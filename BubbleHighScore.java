import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class BubbleHighScore extends Canvas
{
  public static final String HIGHSCORE_STG = "BSHG";
  private int[] m_scores = null;
  private int m_myscore;
  private Image m_back = null;
  private Image m_offscreen = null;
  private BubbleSmile m_bs = null;

  public BubbleHighScore(BubbleSmile paramBubbleSmile, int paramInt)
  {
    this.m_bs = paramBubbleSmile;
    this.m_myscore = paramInt;
    this.m_scores = new int[4];
    this.m_scores[0] = 0;
    this.m_scores[1] = 0;
    this.m_scores[2] = 0;
    this.m_scores[3] = this.m_myscore;
    ReadHighScore(this.m_scores, 3);
    SortHighScore(this.m_scores);
    if (paramInt != -1)
      WriteHighScore(this.m_scores, 3);
    try
    {
      this.m_back = Image.createImage("/mainback.png");
      this.m_offscreen = BubbleSmile.m_img_off;
    }
    catch (IOException localIOException)
    {
    }
  }

  public void paint(Graphics paramGraphics)
  {
    int i = 0;
    try
    {
      Graphics localGraphics = this.m_offscreen.getGraphics();
      localGraphics.drawImage(this.m_back, 0, 0, 20);
      localGraphics.setClip(10, 5, 111, 21);
      localGraphics.drawImage(BubbleSmile.GetImage(), 10, -83, 20);
      if (this.m_myscore == -1)
        i = 50;
      else
        i = 33;
      for (int j = 0; j < 3; ++j)
      {
        BubbleSmile.DrawNumber(localGraphics, 25, i + j * 20, j + 1, 1);
        BubbleSmile.DrawNumber(localGraphics, 45, i + j * 20, this.m_scores[j], 1);
      }
      if (this.m_myscore != -1)
      {
        localGraphics.setClip(24, 90, 82, 16);
        localGraphics.drawImage(BubbleSmile.GetImage(), 23, 20, 20);
        int k = 0;
        int l = this.m_myscore;
        while (l > 0)
        {
          ++k;
          l /= 10;
        }
        BubbleSmile.DrawNumber(localGraphics, 64 - k * 10 / 2, 109, this.m_myscore, 1);
      }
      paramGraphics.drawImage(this.m_offscreen, 0, 0, 20);
    }
    catch (NullPointerException localNullPointerException)
    {
    }
  }

  public void keyPressed(int paramInt)
  {
    Final();
  }

  public void Final()
  {
    this.m_offscreen = null;
    this.m_scores = null;
    this.m_back = null;
    System.gc();
    this.m_bs.SetCurrent(new BubbleLogo(this.m_bs));
    this.m_bs = null;
    System.gc();
  }

  public static final boolean ReadHighScore(int[] paramArrayOfInt, int paramInt)
  {
    RecordStore localRecordStore = null;
    byte[] arrayOfByte = new byte[4 * paramInt];
    for (int i = 0; i < paramInt; ++i)
      paramArrayOfInt[i] = 0;
    try
    {
      localRecordStore = RecordStore.openRecordStore("BSHG", false);
      if (localRecordStore == null)
      {
        arrayOfByte = null;
        return false;
      }
      localRecordStore.getRecord(1, arrayOfByte, 0);
      for (int k = 0; k < paramInt; ++k)
      {
        int j = k * 4;
        paramArrayOfInt[k] = (arrayOfByte[j] & 0xFF);
        paramArrayOfInt[k] |= (arrayOfByte[(j + 1)] & 0xFF) << 8;
        paramArrayOfInt[k] |= (arrayOfByte[(j + 2)] & 0xFF) << 16;
        paramArrayOfInt[k] |= (arrayOfByte[(j + 3)] & 0xFF) << 24;
      }
      localRecordStore.closeRecordStore();
    }
    catch (InvalidRecordIDException localInvalidRecordIDException)
    {
      localRecordStore = null;
      arrayOfByte = null;
      return false;
    }
    catch (RecordStoreException localRecordStoreException)
    {
      localRecordStore = null;
      arrayOfByte = null;
      return false;
    }
    localRecordStore = null;
    arrayOfByte = null;
    return true;
  }

  public static final boolean WriteHighScore(int[] paramArrayOfInt, int paramInt)
  {
    RecordStore localRecordStore = null;
    byte[] arrayOfByte = new byte[4 * paramInt];
    try
    {
      DeleteHighScore();
      localRecordStore = RecordStore.openRecordStore("BSHG", true);
      for (int j = 0; j < paramInt; ++j)
      {
        int i = j * 4;
        arrayOfByte[i] = (byte)(paramArrayOfInt[j] & 0xFF);
        arrayOfByte[(i + 1)] = (byte)(paramArrayOfInt[j] >> 8 & 0xFF);
        arrayOfByte[(i + 2)] = (byte)(paramArrayOfInt[j] >> 16 & 0xFF);
        arrayOfByte[(i + 3)] = (byte)(paramArrayOfInt[j] >> 24 & 0xFF);
      }
      int k = localRecordStore.addRecord(arrayOfByte, 0, arrayOfByte.length);
      localRecordStore.closeRecordStore();
    }
    catch (RecordStoreException localRecordStoreException)
    {
      localRecordStore = null;
      arrayOfByte = null;
      return false;
    }
    localRecordStore = null;
    arrayOfByte = null;
    return true;
  }

  public static final void SortHighScore(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int k = i - 1; k >= 1; --k)
      for (int l = k - 1; l >= 0; --l)
      {
        if (paramArrayOfInt[l] >= paramArrayOfInt[k])
          continue;
        int j = paramArrayOfInt[l];
        paramArrayOfInt[l] = paramArrayOfInt[k];
        paramArrayOfInt[k] = j;
      }
  }

  public static final void DeleteHighScore()
  {
    try
    {
      RecordStore.deleteRecordStore("BSHG");
    }
    catch (Exception localException)
    {
    }
  }
}