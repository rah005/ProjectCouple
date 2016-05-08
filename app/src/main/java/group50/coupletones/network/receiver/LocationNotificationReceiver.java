package group50.coupletones.network.receiver;

import android.content.Context;
import android.content.Intent;
import group50.coupletones.CoupleTones;
import group50.coupletones.R;
import group50.coupletones.controller.MainActivity;
import group50.coupletones.network.message.Message;
import group50.coupletones.network.message.MessageReceiver;
import group50.coupletones.network.message.MessageType;
import group50.coupletones.util.Identifiable;

/**
 * @author Sharmaine Manalo
 * @since 5/5/16
 */

/**
 * Location notification receiver class
 */
public class LocationNotificationReceiver implements MessageReceiver, Identifiable {

  private final Context context;

  private final CoupleTones app;

  /**
   * Location Notification Receiver
   * @param app
   * @param context
   */
  public LocationNotificationReceiver(CoupleTones app, Context context) {
    this.context = context;
    this.app = app;
  }

  /**
   * onReceive
   * @param message The incoming message object that contains data from the server
   */
  @Override
  public void onReceive(Message message) {
    Notification notification = new Notification(context);
    notification.setTitle(context.getString(R.string.app_name));
    notification.setMsg(app.getLocalUser().getPartner().getName() + " " +
        context.getString(R.string.partner_visited_text) + " " + message.getString("name"));

    Intent intent = new Intent(context, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    notification.setIntent(intent);
    notification.show();
  }

  /**
   * Gets the ID
   * @return
   */
  @Override
  public String getId() {
    return MessageType.RECEIVE_MAP_NOTIFY.value;
  }
}
