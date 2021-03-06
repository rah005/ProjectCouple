package group50.coupletones.network.sync;

import group50.coupletones.util.properties.Properties;
import group50.coupletones.util.properties.Property;

/**
 * @author Henry Mao
 * @since 5/27/16
 */
public interface Sync {
  //TODO: Recomment
  /**
   * Hooks listeners to every field in a class annotated with @Watch.
   * <p>
   * When the database for this field changes, the field itself will be automatically updated.
   *
   * @return The Properties object instance
   */
  Sync watch(Property<?> property);

  /**
   * Subscribes a field in the class to receive updates from the database automatically.
   *
   * @param properties The name of the field
   * @return Self instance
   */
  Sync watchAll(Properties properties);

  /**
   * Sends an update to the server
   *
   * @param property The property
   * @return Self instance
   */
  Sync update(Property property);

  Sync parent();

  Sync child(String name);
}
