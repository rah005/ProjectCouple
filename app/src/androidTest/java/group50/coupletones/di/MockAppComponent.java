package group50.coupletones.di;

import dagger.Component;
import group50.coupletones.di.module.GeocoderModule;
import group50.coupletones.di.module.ProximityModule;

import javax.inject.Singleton;

/**
 * The dependency injection component for the entire app using mocks.
 *
 * @author Henry Mao
 * @since 28/4/2016
 */
@Singleton
@Component(
  modules = {
    MockAuthenticatorModule.class,
    MockApplicationModule.class,
    MockNetworkModule.class,
    ProximityModule.class,
    GeocoderModule.class
  }
)
public interface MockAppComponent extends AppComponent {
}
