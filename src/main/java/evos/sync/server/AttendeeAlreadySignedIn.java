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
package evos.sync.server;

/**
 * Exception to be thrown if the attendee is just reconnecting.
 * @author Lukas Hannigbrinck
 */
public class AttendeeAlreadySignedIn extends Exception {

    public AttendeeAlreadySignedIn() {
        super();
    }

    public AttendeeAlreadySignedIn(String message) {
        super(message);
    }

    public AttendeeAlreadySignedIn(String message, Throwable cause) {
        super(message, cause);
    }

    public AttendeeAlreadySignedIn(Throwable cause) {
        super(cause);
    }
}
