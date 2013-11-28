import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Cursor
{
  public static final int POSITION_CNT = 3;
  public static final byte CURSOR_LEFT = 0;
  public static final byte CURSOR_RIGHT = 1;
  public static final byte CURSOR_TOP = 2;
  public static final byte CURSOR_BOTTOM = 3;
  public static final byte CURSOR_IMG_WIDTH = 31;
  public static final byte CURSOR_IMG_HEIGHT = 28;
  private static final int ANGLE_VALUE = 360;
  private boolean m_direction = true;
  private byte m_c = 3;
  private byte m_r = 4;
  private byte[] m_carr = new byte[3];
  private byte[] m_rarr = new byte[3];
  private short[] m_xarr = new short[3];
  private short[] m_yarr = new short[3];
  private int[] m_colors = { 2816800, 2667809, 2518306 };
  private int m_color_index = 0;
  private Image m_img = null;
  private boolean m_status = false;

  public Cursor()
  {
    try
    {
      this.m_img = Image.createImage("/cursor.png");
    }
    catch (IOException localIOException)
    {
    }
  }

  public void Term()
  {
    this.m_carr = null;
    this.m_rarr = null;
    this.m_xarr = null;
    this.m_yarr = null;
    this.m_img = null;
  }

  public short GetLeft()
  {
    if (this.m_direction == true)
      return (short)(this.m_xarr[2] - 20);
    return (short)(this.m_xarr[0] - 20);
  }

  public short GetRight()
  {
    return (short)(this.m_xarr[1] + 15 + 20);
  }

  public short GetTop()
  {
    return this.m_yarr[0];
  }

  public short GetBottom()
  {
    return (short)(this.m_yarr[1] + 15 + 20);
  }

  public boolean IsIn(byte paramByte1, byte paramByte2)
  {
    return (((paramByte1 == this.m_carr[0]) || (paramByte1 == this.m_carr[1]) || (paramByte1 == this.m_carr[2]))) && (((paramByte2 == this.m_rarr[0]) || (paramByte2 == this.m_rarr[1]) || (paramByte2 == this.m_rarr[2])));
  }

  public short GetAt(int paramInt)
  {
    int i = (short)this.m_carr[paramInt];
    i = (short)(i | this.m_rarr[paramInt] << 8);
    return i;
  }

  public boolean Move(byte paramByte)
  {
    int i = this.m_c;
    int j = this.m_r;
    if (this.m_direction == true)
    {
      if ((paramByte == 2) && (this.m_r == 0))
        return false;
      if ((paramByte == 0) && (this.m_c == 0))
        return false;
      if ((paramByte == 1) && (this.m_c == 6))
        return false;
      if ((paramByte == 3) && (this.m_r == 7))
        return false;
      switch (paramByte)
      {
      case 0:
        if (this.m_r % 2 == 0)
          this.m_c = (byte)(this.m_c - 1);
        this.m_r = (byte)(this.m_r + 1);
        break;
      case 1:
        if (this.m_r % 2 != 0)
          this.m_c = (byte)(this.m_c + 1);
        this.m_r = (byte)(this.m_r + 1);
        break;
      case 3:
        this.m_r = (byte)(this.m_r + 2);
      case 2:
      }
    }
    else
    {
      if ((paramByte == 2) && (this.m_r == 1))
        return false;
      if ((paramByte == 0) && (this.m_c == 0))
        return false;
      if ((paramByte == 1) && (this.m_c == 6))
        return false;
      if ((paramByte == 3) && (this.m_r == 8))
        return false;
      switch (paramByte)
      {
      case 0:
        if (this.m_r % 2 == 0)
          this.m_c = (byte)(this.m_c - 1);
        this.m_r = (byte)(this.m_r - 1);
        break;
      case 1:
        if (this.m_r % 2 != 0)
          this.m_c = (byte)(this.m_c + 1);
        this.m_r = (byte)(this.m_r - 1);
        break;
      case 3:
        break;
      case 2:
        this.m_r = (byte)(this.m_r - 2);
      }
    }
    if ((this.m_r < 0) || (this.m_r >= 9) || (this.m_c < 0) || (this.m_c >= 7))
    {
      this.m_r = j;
      this.m_c = i;
      return false;
    }
    this.m_direction = (!this.m_direction);
    return true;
  }

  public void Draw(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    this.m_status = (!this.m_status);
    CalcBubblePosition();
    CalcBubblePixelPoint(paramBubbleGame);
    if (this.m_direction == true)
      DrawUpperCursor(paramGraphics);
    else
      DrawBottomCursor(paramGraphics);
  }

  private void DrawUpperCursor(Graphics paramGraphics)
  {
    int i;
    if (!this.m_status)
      i = 31;
    else
      i = 0;
    paramGraphics.setClip(this.m_xarr[2], this.m_yarr[0], 31, 28);
    paramGraphics.drawImage(this.m_img, this.m_xarr[2] - i, this.m_yarr[0] - 28, 20);
    paramGraphics.setClip(0, 0, 128, 128);
  }

  private void DrawBottomCursor(Graphics paramGraphics)
  {
    int i;
    if (!this.m_status)
      i = 31;
    else
      i = 0;
    paramGraphics.setClip(this.m_xarr[0], this.m_yarr[0], 31, 28);
    paramGraphics.drawImage(this.m_img, this.m_xarr[0] - i, this.m_yarr[0], 20);
    paramGraphics.setClip(0, 0, 128, 128);
  }

  public void CalcBubblePosition()
  {
    if (this.m_direction == true)
    {
      this.m_carr[0] = this.m_c;
      this.m_rarr[0] = this.m_r;
      if (this.m_r % 2 == 0)
      {
        this.m_carr[1] = this.m_c;
        this.m_rarr[1] = (byte)(this.m_r + 1);
        this.m_carr[2] = (byte)(this.m_c - 1);
        this.m_rarr[2] = this.m_rarr[1];
      }
      else
      {
        this.m_carr[1] = (byte)(this.m_c + 1);
        this.m_rarr[1] = (byte)(this.m_r + 1);
        this.m_carr[2] = this.m_c;
        this.m_rarr[2] = this.m_rarr[1];
      }
    }
    else
    {
      if (this.m_r % 2 == 0)
      {
        this.m_carr[0] = (byte)(this.m_c - 1);
        this.m_rarr[0] = (byte)(this.m_r - 1);
        this.m_carr[1] = this.m_c;
        this.m_rarr[1] = this.m_rarr[0];
      }
      else
      {
        this.m_carr[0] = this.m_c;
        this.m_rarr[0] = (byte)(this.m_r - 1);
        this.m_carr[1] = (byte)(this.m_c + 1);
        this.m_rarr[1] = this.m_rarr[0];
      }
      this.m_carr[2] = this.m_c;
      this.m_rarr[2] = this.m_r;
    }
  }

  public void CalcBubblePixelPoint(BubbleGame paramBubbleGame)
  {
    for (int j = 0; j < 3; ++j)
    {
      int i = paramBubbleGame.Pos2Pixel(this.m_carr[j], this.m_rarr[j]);
      this.m_xarr[j] = (short)((i & 0xFFFF) - 1);
      this.m_yarr[j] = (short)(i >> 16 & 0xFFFF);
      if (this.m_direction != true)
        continue;
      int tmp62_61 = j;
      short[] tmp62_58 = this.m_yarr;
      tmp62_58[tmp62_61] = (short)(tmp62_58[tmp62_61] - 1);
    }
  }

  private void NextColorIndex()
  {
    this.m_color_index = ((this.m_color_index + 1) % 3);
  }

  public void Rotation(BubbleGame paramBubbleGame, boolean paramBoolean)
  {
    CalcBubblePosition();
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    byte[] arrayOfByte1 = { 2, 3, 1, -1, -2, -3 };
    byte[] arrayOfByte2 = { -3, -1, -2, 2, 3, 1 };
    for (int j = 0; j < 3; ++j)
    {
      int i;
      if ((!paramBoolean) && (j == 0))
        i = 2;
      else if (paramBoolean == true)
        i = (j + 1) % 3;
      else
        i = (j - 1) % 3;
      byte b1 = this.m_carr[j];
      byte b2 = this.m_rarr[j];
      Bubble localBubble = localBubblePool.GetBubble(b1, b2);
      localBubble.PutSrcPos(b1, b2);
      localBubble.PutDstPos(this.m_carr[i], this.m_rarr[i]);
      if (this.m_direction == true)
        if (paramBoolean == true)
          localBubble.PutDirection(arrayOfByte1[j]);
        else
          localBubble.PutDirection(arrayOfByte1[(j + 3)]);
      else if (paramBoolean == true)
        localBubble.PutDirection(arrayOfByte2[j]);
      else
        localBubble.PutDirection(arrayOfByte2[(j + 3)]);
      localBubble.PutStatus(3);
    }
  }

  public int GetSaveSize()
  {
    return 3;
  }

  public int Save(byte[] paramArrayOfByte)
  {
    if (this.m_direction)
      paramArrayOfByte[0] = 1;
    else
      paramArrayOfByte[0] = 0;
    paramArrayOfByte[1] = this.m_c;
    paramArrayOfByte[2] = this.m_r;
    return 3;
  }

  public void Load(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte[0] == 1)
      this.m_direction = true;
    else
      this.m_direction = false;
    this.m_c = paramArrayOfByte[1];
    this.m_r = paramArrayOfByte[2];
  }

  public void InvalidateCursorArea(BubbleGame paramBubbleGame)
  {
    int i = GetLeft();
    int j = GetRight();
    int k = GetTop();
    int l = GetBottom();
    paramBubbleGame.repaint(i, k, j - i, l - k);
  }

  public byte DebugGetX()
  {
    return this.m_c;
  }

  public byte DebugGetY()
  {
    return this.m_r;
  }
}