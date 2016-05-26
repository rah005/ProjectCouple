/**
 * @author Henry Mao
 * @since 4/22/16.
 */

package group50.coupletones.auth.user;

import group50.coupletones.controller.tab.favoritelocations.map.location.FavoriteLocation;
import group50.coupletones.network.sync.Sync;
import group50.coupletones.network.sync.Syncable;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a User logged in via Google sign in.
 * Uses data from the GoogleSignInAccount object.
 * <p>
 * Firebase
 */
public class ConcreteUser implements LocalUser {

  /**
   * Object responsible for syncing the object with database
   */
  private final Sync sync;
  /**
   * A subject that can be watched. Helps notify partner change.
   */
  BehaviorSubject<User> partnerSubject = BehaviorSubject.create();
  @Syncable
  private String id;
  /**
   * Name of the user
   */
  @Syncable
  private String name;
  /**
   * Email of the user
   */
  @Syncable
  private String email;
  /**
   * User's partner
   */
  private User partner;
  /**
   * The user's list of favorite location.
   */
  @Syncable
  private List<FavoriteLocation> favoriteLocations = new LinkedList<>();
  /**
   * The ID of the user's partner
   */
  @Syncable
  private String partnerId;
  /**
   * A list of all partner Ids who is trying to request partnership
   * with this user.
   */
  @Syncable
  private List<String> partnerRequests = new LinkedList<>();

  /**
   * Creates a ConcreteUser
   * @param sync The sync object, with a database reference for this user.
   */
  public ConcreteUser(Sync sync) {
    this.sync = sync
      .watch(this)
      .subscribeAll();

    // Update the Partner object when partnerId changes
    sync
      .getObservable("partnerId", String.class)
      .subscribe(this::resetPartner);
  }

  /**
   * @return The id of the user
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * @return The name of the user
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * @return The email of the user
   */
  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public List<FavoriteLocation> getFavoriteLocations() {
    return favoriteLocations != null ? Collections.unmodifiableList(favoriteLocations) : Collections.emptyList();
  }

  /**
   * Adds a favorite location
   * @param location The location to add
   */
  @Override
  public void addFavoriteLocation(FavoriteLocation location) {
    favoriteLocations.add(location);
    sync.publish("favoriteLocations");
  }

  /**
   * Removes a favorite location
   * @param location The location to remove
   */
  @Override
  public void removeFavoriteLocation(FavoriteLocation location) {
    favoriteLocations.remove(location);
    sync.publish("favoriteLocations");
  }

  /**
   * @return The partner of the user
   */
  @Override
  public User getPartner() {
    resetPartner(partnerId);
    return partner;
  }

  /**
   * Sets partner
   * @param partnerId The partner's ID to set
   */
  @Override
  public void setPartner(String partnerId) {
    this.partnerId = partnerId;
    sync.publish("partnerId");
    resetPartner(partnerId);
  }

  /**
   * Lazy initialize or destroy partner from ID
   */
  private void resetPartner(String partnerId) {
    if (partnerId != null) {
      // An update has occurred. Attempt to reconstruct the partner object.
      if (partner == null || !partnerId.equals(partner.getId())) {
        // Partner has changed
        partner = new Partner(sync.sibling(partnerId));
        partnerSubject.onNext(partner);
      }
    } else if (partner != null) {
      partner = null;
      partnerSubject.onNext(partner);
    }
  }

  /**
   * Requests to partner with this user.
   * @param requester The user sending the request
   */
  @Override
  public void requestPartner(User requester) {
    partnerRequests.add(0, requester.getId());
    sync.publish("partnerRequests");
  }

  /**
   * Handles the partner request, either accepting or rejecting it
   * @param partnerId The partner ID
   * @param accept True if accept, false if reject
   */
  @Override
  public void handlePartnerRequest(String partnerId, boolean accept) {
    if (accept) {
      setPartner(partnerId);
    }

    partnerRequests.remove(partnerId);
    sync.publish("partnerRequests");
  }

  @Override
  public Observable<User> getPartnerObservable() {
    return partnerSubject.startWith(partner);
  }

  @Override
  public <T> Observable<T> getObservable(String name) {
    return (Observable<T>) sync.getObservable(name);
  }
}
