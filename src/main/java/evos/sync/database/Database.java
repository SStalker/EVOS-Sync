/**
 * This file is part of EVOS-Sync.
 *
 * EVOS-Sync is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EVOS-Sync is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EVOS-Sync.  If not, see <http://www.gnu.org/licenses/>.
 */
package evos.sync.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Singleton;

/**
 * This class is used as an abstraction to the database.
 *
 * @author Christian Wansart
 */
@Singleton
public class Database {
    
    private Connection connection;
    
    public Database() {
        Properties databaseProperties = new Properties();
        try (InputStream databasePropertiesStream = this.getClass().getResourceAsStream("/database.properties")) {
            databaseProperties.load(databasePropertiesStream);
            
            Logger.getLogger(Database.class.getName()).log(Level.INFO, "Successfully loaded database.properties file");
        } catch (NullPointerException | IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            String connectionString = "jdbc:mysql://" + databaseProperties.getProperty("host")
                    + ":" + databaseProperties.getProperty("port")
                    + "/evos?user=" + databaseProperties.getProperty("user") + ""
                    + "&password=" + databaseProperties.getProperty("password")
                    + "&useSSL=false&serverTimezone=" + databaseProperties.getProperty("timezone");
            connection = DriverManager.getConnection(connectionString);
            
            Logger.getLogger(Database.class.getName()).log(Level.INFO, "Successfully loaded database");
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
