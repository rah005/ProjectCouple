package group50.coupletones.controller.tab.partnerslocations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import group50.coupletones.R;
import group50.coupletones.controller.MainActivity;
import group50.coupletones.controller.tab.TabFragment;
import group50.coupletones.util.sound.VibeTone;

/**
 * A simple {@link Fragment} subclass for showing vibe tone assign choice.
 *
 * @author Joanne Cho
 */
public class VibeTonesFragment extends TabFragment<Object> {
  private RecyclerView vibeTonesList;
  private ListAdapterVibeTones adapter;
  /**
   * Index of the partner's favorite location.
   */
  private int locationIndex;


  public VibeTonesFragment() {
    super(Object.class);
  }

  public VibeTonesFragment(int locationIndex) {
    this();
    this.locationIndex = locationIndex;
  }

  /**
   * getResourceID
   *
   * @return - VibeTones fragment
   */
  @Override
  protected int getResourceId() {
    return R.layout.fragment_edit_vibetone;
  }

  /**
   * onCreateView
   *
   * @return - View of app
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_edit_vibetone, container, false);
    vibeTonesList = (RecyclerView) v.findViewById(R.id.vibetone_recycler_view);
    vibeTonesList.setLayoutManager(new LinearLayoutManager(getActivity()));
    adapter = new ListAdapterVibeTones(locationIndex, VibeTone.getVibeTones(), this);
    vibeTonesList.setAdapter(adapter);

    // Clicking back button returns user to Partner's Favorite List
    ImageButton backButton;
    backButton = (ImageButton) v.findViewById(R.id.btn_backarrow);
    backButton.setOnClickListener(view -> ((MainActivity) getActivity()).setFragment(new PartnersFavoritesFragment()));

    return v;
  }
}
