import com.samsung.util.AudioClip;
import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

public class BubbleSmile extends MIDlet
{
  public static Image m_img_etc = null;
  public static Image m_img_off = null;
  public static AudioClip m_snd_down = null;
  public static AudioClip m_snd_back = null;
  public static AudioClip m_snd_remove = null;
  public static AudioClip m_snd_change = null;
  public static AudioClip m_snd_sel = null;
  public static AudioClip m_snd_bonus = null;
  public static AudioClip m_snd_selchange = null;
  public static AudioClip m_snd_cur = null;
  private Display m_disp = null;

  public static Image GetImage()
  {
    return m_img_etc;
  }

  public BubbleSmile()
  {
    try
    {
      m_img_etc = Image.createImage("/etc.png");
      m_img_off = Image.createImage(128, 128);
      m_snd_down = new AudioClip(1, "/1.mmf");
      m_snd_remove = new AudioClip(1, "/2.mmf");
      m_snd_bonus = new AudioClip(1, "/3.mmf");
      m_snd_change = new AudioClip(1, "/4.mmf");
      m_snd_sel = new AudioClip(1, "/5.mmf");
      m_snd_back = new AudioClip(1, "/6.mmf");
      m_snd_selchange = new AudioClip(1, "/7.mmf");
    }
    catch (IOException localIOException)
    {
    }
  }

  public void startApp()
  {
    SetCurrent(new BubbleLogo(this));
  }

  public void pauseApp()
  {
    Final();
  }

  public void destroyApp(boolean paramBoolean)
  {
    Final();
  }

  public void Final()
  {
    Displayable localDisplayable = this.m_disp.getCurrent();
    String str = localDisplayable.getClass().getName();
    if (true == str.equals("BubbleGame"))
    {
      BubbleGame localBubbleGame = (BubbleGame)localDisplayable;
      localBubbleGame.DeleteRS();
      localBubbleGame.Save();
      localBubbleGame.Term();
    }
    m_img_etc = null;
    m_img_off = null;
    if (m_snd_cur != null)
      m_snd_cur.stop();
    m_snd_change = null;
    m_snd_down = null;
    m_snd_remove = null;
    m_snd_sel = null;
    m_snd_back = null;
    m_snd_bonus = null;
    m_snd_cur = null;
    System.gc();
  }

  public void SetCurrent(Displayable paramDisplayable)
  {
    this.m_disp.setCurrent(paramDisplayable);
  }

  public static void DrawNumber(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int l = 0;
    switch (paramInt4)
    {
    case 0:
      i = 10;
      j = 15;
      k = 0;
      l = 0;
      break;
    case 1:
      i = 12;
      j = 17;
      k = 0;
      l = 109;
      break;
    case 3:
      i = 7;
      j = 10;
      k = 41;
      l = 15;
      break;
    case 4:
      i = 7;
      j = 10;
      k = 33;
      l = 15;
    case 2:
    }
    int i1 = paramInt3;
    int i3 = 10000;
    while (i3 > 0)
    {
      if (paramInt3 >= i3)
      {
        int i2 = i1 / i3;
        i1 %= i3;
        paramGraphics.setClip(paramInt1, paramInt2, i, j);
        paramGraphics.drawImage(m_img_etc, paramInt1 - k - i * i2, paramInt2 - l, 20);
        paramInt1 += i;
      }
      i3 /= 10;
    }
    paramGraphics.setClip(0, 0, 128, 128);
  }

  public static void PlaySound(int paramInt)
  {
    if (!BubbleOption.m_effect)
      return;
    if (m_snd_cur != null)
      m_snd_cur.stop();
    switch (paramInt)
    {
    case 1:
      m_snd_down.play(1, 3);
      m_snd_cur = m_snd_down;
      break;
    case 2:
      m_snd_remove.play(1, 3);
      m_snd_cur = m_snd_remove;
      break;
    case 3:
      m_snd_change.play(1, 3);
      m_snd_cur = m_snd_change;
      break;
    case 4:
      m_snd_sel.play(1, 3);
      m_snd_cur = m_snd_sel;
      break;
    case 5:
      m_snd_back.play(1, 3);
      m_snd_cur = m_snd_back;
      break;
    case 6:
      m_snd_bonus.play(1, 3);
      m_snd_cur = m_snd_bonus;
      break;
    case 7:
      m_snd_selchange.play(1, 3);
      m_snd_cur = m_snd_selchange;
    }
  }
}