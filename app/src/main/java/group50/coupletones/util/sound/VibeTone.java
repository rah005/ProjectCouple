package group50.coupletones.util.sound;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import group50.coupletones.CoupleTones;
import group50.coupletones.R;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * @Author Joseph
 * @Since 5/21/16
 */
public class VibeTone {
  public static final int MAX_VIBETONE_COUNT = 10;
  public static final int ARRIVAL_VIBETONE = 10;
  public static final int DEPARTURE_VIBETONE = 11;
  private static VibeTone[] tones;
  private static long delay = 2000;//The amount of time in milliseconds to wait between arrival/departure global sound and specific sound.
  @Inject
  public CoupleTones app;
  private Ringtone sound;
  private long[] vibration;
  private String name;
  private Uri arrivalFile, departureFile;
  private Ringtone arrivalTone, departureTone;
  private VibeTone(int resource, long[] vibration, String name) {
    CoupleTones.global().inject(this);
    this.vibration = vibration;
    this.name = name;
    Uri file = Uri.parse("android.resource://" + app.getApplicationContext().getPackageName() + "/" + resource);
    arrivalFile = Uri.parse("android.resource://" + app.getApplicationContext().getPackageName() + "/" + R.raw.arrivaltone);
    departureFile = Uri.parse("android.resource://" + app.getApplicationContext().getPackageName() + "/" + R.raw.departuretone);
    sound = RingtoneManager.getRingtone(app.getApplicationContext(), file);
    arrivalTone = RingtoneManager.getRingtone(app.getApplicationContext(), arrivalFile);
    departureTone = RingtoneManager.getRingtone(app.getApplicationContext(), departureFile);
  }
  public static void loadTones() {
    tones = new VibeTone[]{
      new VibeTone(R.raw.vibetone1, new long[]{0, 100}, "Pikachu"),//Index 0 / default tone.
      new VibeTone(R.raw.vibetone2, new long[]{0, 100, 400}, "Coin"),//Index 1.
      new VibeTone(R.raw.vibetone3, new long[]{0, 100, 400, 100}, "Super Mario"),//Index 2.
      new VibeTone(R.raw.vibetone4, new long[]{0, 400, 100}, "Pokemon Battle"),//Index 3.
      new VibeTone(R.raw.vibetone5, new long[]{0, 400}, "Kim Possible"),//Index 4.
      new VibeTone(R.raw.vibetone6, new long[]{0, 100}, "Kakao"),//Index 5.
      new VibeTone(R.raw.vibetone7, new long[]{0, 200, 400}, "Bling"),//Index 6.
      new VibeTone(R.raw.vibetone8, new long[]{0, 200, 400, 300}, "1 Up"),//Index 7.
      new VibeTone(R.raw.vibetone9, new long[]{0, 100, 300}, "Windows"),//Index 8.
      new VibeTone(R.raw.vibetone10, new long[]{0, 300, 100}, "Zelda"),//Index 9.
      new VibeTone(R.raw.arrivaltone, new long[]{0, 200}, "Arrival"),//Index 10 / Arrival.
      new VibeTone(R.raw.departuretone, new long[]{0, 300}, "Departure"),//Index 11 / Departure.
    };
  }
  /**
   * @return the default vibetone.
   */
  public static VibeTone getDefaultTone() {
    return tones[0];
  }
  /**
   * @return the ith vibetone.
   */
  public static VibeTone getTone(int which) {
    if (which < 0 || which >= MAX_VIBETONE_COUNT) return tones[0];
    return tones[which];
  }
  public static List<VibeTone> getVibeTones() {
    List<VibeTone> availableTones = new LinkedList<>();
    for (int i = 0; i < MAX_VIBETONE_COUNT; i++)
      availableTones.add(tones[i]);
    return Collections.unmodifiableList(availableTones);
  }
  public void playArrival() {
    try {
      arrivalTone.play();
      Thread.sleep(delay);
      play();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  public void playDeparture() {
    try {
      departureTone.play();
      Thread.sleep(delay);
      play();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  private void play() {
    if (app.getLocalUser().getVibrationSetting())
      playVibrate();
    if (app.getLocalUser().getTonesSetting())
      playSound();
  }
  public void playVibrate() {
    Vibrator vib = (Vibrator) app.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    vib.vibrate(vibration, 0);
  }
  public void playSound() {
    sound.play();
  }
  public String getName() {
    return name;
  }
  public int getIndex() {
    for (int i = 0; i < tones.length; i++) {
      if (tones[i].equals(this)) return i;
    }
    throw new RuntimeException("VibeTones should only exist as one of the elements contained in tones.");
  }
  @Override
  public int hashCode() {
    return getIndex();
  }
  @Override
  public boolean equals(Object object) {
    if (object instanceof VibeTone) {
      VibeTone other = (VibeTone) object;
      if (sound != null && other.sound != null && !sound.equals(other.sound))
        return false;
      if ((sound == null && other.sound != null) || (sound != null && other.sound == null))
        return false;
      if (!Arrays.equals(vibration, other.vibration)) return false;
      return true;
    }
    return false;
  }
}
