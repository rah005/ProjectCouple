/**
 * @author Henry Mao
 * @since 4/22/16.
 */

package group50.coupletones.controller;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import group50.coupletones.CoupleTones;
import group50.coupletones.R;
import group50.coupletones.auth.Authenticator;
import group50.coupletones.auth.user.User;
import group50.coupletones.di.InstanceComponent;
import group50.coupletones.di.module.ContextModule;
import group50.coupletones.util.Taggable;

import javax.inject.Inject;

/**
 * The LoginActivity controls the login page of the app.
 */
public class LoginActivity extends AppCompatActivity implements Taggable {

  @Inject
  public CoupleTones app;
  /**
   * The object used for handling authentication
   */
  public Authenticator<User, String> auth;
  protected InstanceComponent component;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // Dependency Injection
    CoupleTones.global().inject(this);

    component = CoupleTones
      .instanceComponentBuilder()
      .contextModule(new ContextModule(this))
      .build();

    // Create Google Authenticator for automatic sign in.
    auth = component.auth();
    auth.onSuccess(this::onUserLogin);
    auth.autoSignIn();

    TextView coupleTones_text = (TextView) findViewById(R.id.sign_in_button);
    Typeface pierSans = Typeface.createFromAsset(getAssets(), getString(R.string.pier_sans));
    coupleTones_text.setTypeface(pierSans);
    findViewById(R.id.sign_in_button).setOnClickListener(evt -> auth.signIn(this));
  }

  @Override
  protected void onStart() {
    super.onStart();
    auth.connect();
  }

  @Override
  protected void onStop() {
    super.onStop();
    auth.disconnect();
  }

  /**
   * Handles the user login event by switching to MainActivity upon
   * successful login.
   * @param user - The user that logged in
   * @return - The user
   */
  private User onUserLogin(User user) {
    //TODO: Better design to signify how the sign in failed. User should never be null?
    if (user != null) {
      // Star the main activity
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
    } else {
      //TODO: Use string constant
      Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
    }
    return user;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    auth.onActivityResult(requestCode, resultCode, data);
  }
}
