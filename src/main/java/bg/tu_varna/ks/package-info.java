/**
 * Root package of the calendar information system.
 * <p>
 * Contains the application's entry point -
 * {@link bg.tu_varna.ks.Application}, which starts the REPL loop and
 * forwards every line of user input to the command subsystem.
 * </p>
 *
 * <h2>Architecture</h2>
 * <p>
 * The project is split into the following logical layers:
 * </p>
 * <ul>
 *   <li>{@link bg.tu_varna.ks.contracts} - interfaces defining the
 *       common abstractions in the system.</li>
 *   <li>{@link bg.tu_varna.ks.models} - domain models (Calendar, Event).</li>
 *   <li>{@link bg.tu_varna.ks.command} - command definition and dispatching.</li>
 *   <li>{@link bg.tu_varna.ks.command.utility} - system commands (open, save, etc.).</li>
 *   <li>{@link bg.tu_varna.ks.command.events} - commands operating on events.</li>
 *   <li>{@link bg.tu_varna.ks.command.factories} - command factory.</li>
 *   <li>{@link bg.tu_varna.ks.command.files} - data persistence (JAXB).</li>
 *   <li>{@link bg.tu_varna.ks.command.files.adapters} - JAXB adapters for date/time.</li>
 * </ul>
 *
 * <h2>Patterns used</h2>
 * <ul>
 *   <li><b>Singleton</b> - {@link bg.tu_varna.ks.models.Calendar},
 *       {@link bg.tu_varna.ks.command.factories.CommandFactory},
 *       {@link bg.tu_varna.ks.command.files.AppData}.</li>
 *   <li><b>Builder</b> - {@link bg.tu_varna.ks.models.Event.EventBuilder}.</li>
 *   <li><b>Factory</b> - {@link bg.tu_varna.ks.command.factories.CommandFactory}.</li>
 *   <li><b>Command / Strategy</b> -
 *       {@link bg.tu_varna.ks.contracts.Executable} and all of its
 *       implementations.</li>
 * </ul>
 *
 * @author pzlmchv 24621854
 * @version 9.11
 */
package bg.tu_varna.ks;
