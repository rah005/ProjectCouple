package group50.coupletones.controller.tab.partnerslocations;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import group50.coupletones.CoupleTones;
import group50.coupletones.R;
import group50.coupletones.auth.user.Partner;
import group50.coupletones.controller.tab.favoritelocations.map.location.VisitedLocationEvent;
import group50.coupletones.util.FormatUtility;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

import javax.inject.Inject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Joanne Cho
 * @since 5/5/16
 */

/**
 * Partner Locations List Adapter Class
 */
public class ListAdapterPartnerVisited extends RecyclerView.Adapter<ListAdapterPartnerVisited.ListViewHolder> {
  @Inject
  public FormatUtility formatUtility;

  private List<VisitedLocationEvent> locations = new LinkedList<>();
  private LayoutInflater inflater;
  private CompositeSubscription subs = new CompositeSubscription();

  /**
   * Partner list adapter
   *
   * @param partnerObservable - The partnerObservable object
   */
  public ListAdapterPartnerVisited(Observable<Partner> partnerObservable, Context context) {
    this.inflater = LayoutInflater.from(context);

    this.subs.add(
      partnerObservable
        .filter(partner -> partner != null)
        .subscribe(partner -> {
          // Initial list
          setLocations(partner.getVisitedLocations());

          partner
            .observable("visitedLocations", List.class)
            .subscribe(this::setLocations);
        })
    );

    CoupleTones.global().inject(this);
  }

  private void setLocations(List<VisitedLocationEvent> locations) {
    this.locations = locations != null ? locations : Collections.emptyList();
    notifyDataSetChanged();
  }

  /**
   * List view holder for partner locations
   *
   * @param parent - ViewGroup
   * @return - ListViewholder
   */
  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = inflater.inflate(R.layout.partner_list_item, parent, false);
    return new ListViewHolder(v);
  }

  /**
   * View holder for partner locations
   *
   * @param holder   - ListViewHolder
   * @param position - Partner location's position
   */
  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {
    VisitedLocationEvent visitedLocation = locations.get(position);
    holder.name.setText(visitedLocation.getName());
    holder.address.setText(formatUtility.formatAddress(visitedLocation.getAddress()));
    holder.icon.setImageResource(R.drawable.target_icon);
    holder.arrivalValue.setText(formatUtility.formatDate(visitedLocation.getTimeVisited()));

    if (visitedLocation.hasDeparted())
      holder.departureValue.setText(formatUtility.formatDate(visitedLocation.getTimeLeft()));
    else
      holder.departureValue.setText("");
  }

  /**
   * Gets Item Count
   *
   * @return - number of items
   */
  @Override
  public int getItemCount() {
    return locations.size();
  }

  /**
   * Adds Recycler View to List View Holder
   */
  class ListViewHolder extends RecyclerView.ViewHolder {

    private TextView name, address, arrivalValue, departureValue;
    private ImageView icon;
    private View container;
    private CardView cv;

    /**
     * ListViewHolder Constructor
     *
     * @param itemView - View
     */
    public ListViewHolder(View itemView) {
      super(itemView);
      cv = (CardView) itemView.findViewById(R.id.cv);
      name = (TextView) itemView.findViewById(R.id.list_item_name);
      address = (TextView) itemView.findViewById(R.id.list_item_address);
      icon = (ImageView) itemView.findViewById(R.id.list_vibetone_icon);
      arrivalValue = (TextView) itemView.findViewById(R.id.list_item_arrival);
      departureValue = (TextView) itemView.findViewById(R.id.list_item_departure);
      container = itemView.findViewById(R.id.list_item);
    }
  }
}