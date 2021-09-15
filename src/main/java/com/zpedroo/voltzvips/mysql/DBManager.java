package com.zpedroo.voltzvips.mysql;

import com.zpedroo.voltzvips.managers.VipManager;
import com.zpedroo.voltzvips.objects.PlayerData;
import com.zpedroo.voltzvips.objects.PlayerVip;
import com.zpedroo.voltzvips.objects.Vip;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DBManager {

    public void saveData(PlayerData data) {
        if (contains(data.getUUID().toString(), "uuid")) {
            String query = "UPDATE `" + DBConnection.TABLE + "` SET" +
                    "`uuid`='" + data.getUUID().toString() + "', " +
                    "`vips`='" + serializeVips(data.getVIPs()) + "', " +
                    "`selected`='" + (data.getSelectedVip() == null ? "" : data.getSelectedVip().getVip().getName()) + "';";
            executeUpdate(query);
            return;
        }

        String query = "INSERT INTO `" + DBConnection.TABLE + "` (`uuid`, `vips`, `selected`) VALUES " +
                "('" + data.getUUID().toString() + "', " +
                "'" + serializeVips(data.getVIPs()) + "', " +
                "'" + (data.getSelectedVip() == null ? "" : data.getSelectedVip().getVip().getName()) + "');";
        executeUpdate(query);
    }

    public PlayerData loadData(Player player) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `" + DBConnection.TABLE + "` WHERE `uuid`='" + player.getUniqueId().toString() + "';";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                UUID uuid = UUID.fromString(result.getString(1));
                Set<PlayerVip> vips = deserializeVips(result.getString(2));
                String selectedVipName = result.getString(3);

                PlayerVip selectedVIP = null;
                for (PlayerVip playerVIP : vips) {
                    if (!playerVIP.getVip().getName().equals(selectedVipName)) continue;

                    selectedVIP = playerVIP;
                    break;
                }

                return new PlayerData(uuid, vips, selectedVIP);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return new PlayerData(player.getUniqueId(), new HashSet<>(4), null);
    }

    private String serializeVips(Set<PlayerVip> vips) {
        StringBuilder builder = new StringBuilder();

        for (PlayerVip vip : vips) {
            builder.append(vip.getVip().getName()).append("#")
                    .append(vip.getExpiration().toString()).append(",");
        }

        return builder.toString();
    }

    private Set<PlayerVip> deserializeVips(String serialized) {
        Set<PlayerVip> ret = new HashSet<>(4);

        String[] vipsSplit = serialized.split(",");

        for (String pets : vipsSplit) {
            String[] vipInfoSplit = pets.split("#");

            Vip vip = VipManager.getInstance().getVip(vipInfoSplit[0]);
            if (vip == null) continue;
            Long expiration = Long.parseLong(vipInfoSplit[1]);

            ret.add(new PlayerVip(vip, expiration));
        }

        return ret;
    }

    private Boolean contains(String value, String column) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT `" + column + "` FROM `" + DBConnection.TABLE + "` WHERE `" + column + "`='" + value + "';";
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return false;
    }

    private void executeUpdate(String query) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, null, null, statement);
        }
    }

    private void closeConnection(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS `" + DBConnection.TABLE + "` (`uuid` VARCHAR(255), `vips` LONGTEXT, `selected` LONGTEXT, PRIMARY KEY(`uuid`));";
        executeUpdate(query);
    }

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
}