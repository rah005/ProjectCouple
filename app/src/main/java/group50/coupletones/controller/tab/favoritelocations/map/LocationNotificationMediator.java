package group50.coupletones.controller.tab.favoritelocations.map;

import android.util.Log;
import group50.coupletones.CoupleTones;
import group50.coupletones.controller.tab.favoritelocations.map.location.VisitedLocationEvent;
import group50.coupletones.network.fcm.NetworkManager;
import group50.coupletones.util.FormatUtility;
import group50.coupletones.util.Taggable;

import javax.inject.Inject;

/**
 * @author Sharmaine Manalo
 * @since 5/5/16
 */
public class LocationNotificationMediator implements Taggable {

  @Inject
  public CoupleTones app;
  @Inject
  public NetworkManager network;
  @Inject
  public FormatUtility formatUtility;

  /**
   * Proximity Network Handler
   */
  public LocationNotificationMediator() {
    CoupleTones.global().inject(this);
  }

  /**
   * whileInLocation
   * @param location - Visited Location
   */
  public void onEnterLocation(VisitedLocationEvent location) {
    if (app.getLocalUser() != null && app.getLocalUser().getPartner() != null) {
      // Adds the location as a visited location
      app.getLocalUser().arriveVisitedLocation(location);
    } else {
      Log.e(getTag(), "Attempt to send location notification to null partner.");
    }
  }

  public void onLeaveLocation(VisitedLocationEvent location) {
    if (app.getLocalUser() != null && app.getLocalUser().getPartner() != null) {
      // Adds the location as a visited location
      app.getLocalUser().leaveVisitedLocation(location);
    } else {
      Log.e(getTag(), "Attempt to send location notification to null partner.");
    }
  }
}
