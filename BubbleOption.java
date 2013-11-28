import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

class BubbleOption extends Canvas
{
  private static final String OPTION_STG = "BUBBLEOPTION";
  public static boolean m_effect = true;
  private Image m_img = null;
  private BubbleSmile m_bs = null;
  private boolean m_cache = false;

  public BubbleOption(BubbleSmile paramBubbleSmile)
  {
    this.m_bs = paramBubbleSmile;
    try
    {
      Image localImage = Image.createImage("/mainback.png");
      this.m_img = BubbleSmile.m_img_off;
      Graphics localGraphics = this.m_img.getGraphics();
      localGraphics.drawImage(localImage, 0, 0, 20);
      localImage = null;
    }
    catch (IOException localIOException)
    {
    }
    this.m_cache = m_effect;
  }

  public static final boolean ReadOption()
  {
    RecordStore localRecordStore = null;
    byte[] arrayOfByte = new byte[1];
    try
    {
      localRecordStore = RecordStore.openRecordStore("BUBBLEOPTION", true);
      int i = 1;
      localRecordStore.getRecord(i, arrayOfByte, 0);
      if (arrayOfByte[0] == 1)
        m_effect = true;
      else
        m_effect = false;
      localRecordStore.closeRecordStore();
    }
    catch (InvalidRecordIDException localInvalidRecordIDException)
    {
      return false;
    }
    catch (RecordStoreException localRecordStoreException)
    {
      return false;
    }
    return true;
  }

  public static final boolean WriteOption()
  {
    RecordStore localRecordStore = null;
    byte[] arrayOfByte = new byte[1];
    try
    {
      RecordStore.deleteRecordStore("BUBBLEOPTION");
    }
    catch (Exception localException)
    {
    }
    try
    {
      localRecordStore = RecordStore.openRecordStore("BUBBLEOPTION", true);
      if (m_effect == true)
        arrayOfByte[0] = 1;
      else
        arrayOfByte[0] = 0;
      localRecordStore.addRecord(arrayOfByte, 0, 1);
      localRecordStore.closeRecordStore();
    }
    catch (RecordStoreException localRecordStoreException)
    {
      return false;
    }
    return true;
  }

  public void paint(Graphics paramGraphics)
  {
    paramGraphics.drawImage(this.m_img, 0, 0, 20);
    paramGraphics.setClip(19, 2, 91, 24);
    paramGraphics.drawImage(BubbleSmile.GetImage(), 19, -124, 20);
    paramGraphics.setClip(38, 50, 60, 58);
    paramGraphics.drawImage(BubbleSmile.GetImage(), 38, -100, 20);
    int i = 0;
    int j = 0;
    if (this.m_cache == true)
    {
      i = 22;
      j = 71;
    }
    else
    {
      i = 22;
      j = 92;
    }
    paramGraphics.setClip(i, j, 20, 12);
    paramGraphics.drawImage(BubbleSmile.GetImage(), i - 84, j - 70, 20);
  }

  public void keyPressed(int paramInt)
  {
    switch (paramInt)
    {
    case -2:
    case -1:
    case 50:
    case 56:
      BubbleSmile.PlaySound(7);
      this.m_cache = (!this.m_cache);
      super.repaint();
      break;
    case -5:
    case 53:
      BubbleSmile.PlaySound(4);
      m_effect = this.m_cache;
      Final();
      break;
    default:
      Final();
    }
  }

  public void Final()
  {
    this.m_img = null;
    System.gc();
    this.m_bs.SetCurrent(new BubbleLogo(this.m_bs));
    this.m_bs = null;
    System.gc();
  }
}