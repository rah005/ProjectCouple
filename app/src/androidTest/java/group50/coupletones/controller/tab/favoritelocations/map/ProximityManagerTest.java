package group50.coupletones.controller.tab.favoritelocations.map;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.google.android.gms.maps.model.LatLng;
import group50.coupletones.CoupleTones;
import group50.coupletones.auth.user.LocalUser;
import group50.coupletones.controller.tab.favoritelocations.map.location.UserFavoriteLocation;
import group50.coupletones.controller.tab.favoritelocations.map.location.UserVisitedLocation;
import group50.coupletones.di.DaggerMockAppComponent;
import group50.coupletones.di.MockProximityModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

/**
 * @author Joseph Cox
 * @since 5/28/16
 */

/**
 * Proximity manager test for favorite location
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProximityManagerTest {

  private UserFavoriteLocation favLocation;
  private Location loc;
  private MapProximityManager proximity;
  private ProximityObserver mockObserver;

  @Before
  public void setup() {
    // Setup DI
    CoupleTones.setGlobal(
      DaggerMockAppComponent
        .builder()
        .mockProximityModule(new MockProximityModule())
        .build()
    );

    favLocation = new UserFavoriteLocation("", new LatLng(32.880315, -117.236288), 0);
    loc = new Location("");

    // Stub the user
    LocalUser localUser = mock(LocalUser.class);
    // Setup the users favorite location list.
    LinkedList<UserFavoriteLocation> list = new LinkedList<>();
    list.add(favLocation);
    when(localUser.getFavoriteLocations()).thenReturn(list);

    // Stub getLocalUser to always return the mock user above
    when(CoupleTones.global().app().getLocalUser()).thenReturn(localUser);
    when(CoupleTones.global().app().isLoggedIn()).thenReturn(true);

    proximity = new MapProximityManager(CoupleTones.global().app());
    mockObserver = mock(ProximityObserver.class);
    proximity.register(mockObserver);
  }

  //TODO: Organize this test into edge case, normal case, error case
  //list.get(1).setLatitude(32.880292);
  //list.get(1).setLongitude(-117.237329);
  //list.get(2).setLatitude(32.880233);
  //list.get(2).setLongitude(-117.238316);

  @Test
  public void testLocationListenerFarAway() {
    //Normal case - should not pass
    loc.setLatitude(10.0);
    loc.setLongitude(10.0);
    proximity.onLocationChanged(loc);
    verify(mockObserver, never()).onEnterLocation(anyObject());
  }

  @Test
  public void testNotificationNearby() {
    //Normal case - should pass
    loc.setLatitude(32.880351);
    loc.setLongitude(-117.236578);
    proximity.onLocationChanged(loc);
    verify(mockObserver).onEnterLocation(anyObject());
  }

  @Test
  public void testNotificationTimer() {
    ProximityObserver observer = new ProximityObserver() {
      int count = 0;

      @Override
      public void onEnterLocation(UserVisitedLocation location) {
        assert (count == 0);
        count++;
      }
    };
    proximity.register(observer);
    UserFavoriteLocation shouldNotifyOnce = new UserFavoriteLocation();
    proximity.onEnterLocation(shouldNotifyOnce);
    proximity.onEnterLocation(shouldNotifyOnce);
  }
}
