import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Bubble
{
  public static final byte S_NORMAL = 0;
  public static final byte S_NORMAL2 = 1;
  public static final byte S_NORMAL3 = 2;
  public static final byte S_NORMAL4 = 9;
  public static final byte S_MOVE_BEGIN = 3;
  public static final byte S_MOVE_1 = 4;
  public static final byte S_MOVE_2 = 5;
  public static final byte S_MOVE_END = 6;
  public static final byte S_DELETE_BEGIN = 10;
  public static final byte S_DELETE_0 = 11;
  public static final byte S_DELETE_1 = 12;
  public static final byte S_DELETE_2 = 13;
  public static final byte S_DELETE_END = 14;
  public static final byte DIR_1 = 1;
  public static final byte DIR_2 = 2;
  public static final byte DIR_3 = 3;
  public static final byte DIR_4 = -1;
  public static final byte DIR_5 = -2;
  public static final byte DIR_6 = -3;
  public static Random rand = null;
  public byte m_num = -1;
  public byte m_status = 0;
  public byte m_sc;
  public byte m_sr;
  public byte m_dc;
  public byte m_dr;
  public byte m_direction;

  public static final void InitClass()
  {
    rand = new Random();
  }

  public static final void TermClass()
  {
    rand = null;
  }

  public void Randomize()
  {
    this.m_status = 0;
    do
    {
      long l = rand.nextInt();
      if (l < 0L)
        l = -l;
      this.m_num = (byte)(int)(l % 7L);
    }
    while (this.m_num < 0);
  }

  public byte GetStatus()
  {
    return this.m_status;
  }

  public void PutStatus(byte paramByte)
  {
    this.m_status = paramByte;
  }

  public boolean IsDelete()
  {
    return (this.m_status >= 10) && (this.m_status <= 14);
  }

  public boolean IsMoving()
  {
    return (this.m_status >= 3) && (this.m_status <= 6);
  }

  public boolean IsGrin()
  {
    return (this.m_status == 1) || (this.m_status == 2);
  }

  public boolean IsGrinFinished()
  {
    return this.m_status == 9;
  }

  public void NextMovingStatus()
  {
    if (this.m_status >= 6)
      this.m_status = 6;
    else
      this.m_status = (byte)(this.m_status + 1);
  }

  public void NextDeleteStatus()
  {
    if (this.m_status >= 14)
      this.m_status = 14;
    else
      this.m_status = (byte)(this.m_status + 1);
  }

  public byte GetNumber()
  {
    return this.m_num;
  }

  public void PutNumber(byte paramByte)
  {
    this.m_num = paramByte;
  }

  public void Draw(Graphics paramGraphics, Image paramImage, int paramInt1, int paramInt2)
  {
    paramGraphics.setClip(paramInt1, paramInt2, 15, 15);
    int i = 15 * this.m_num;
    switch (this.m_status)
    {
    case 1:
    case 2:
      paramGraphics.drawImage(paramImage, paramInt1 - i, paramInt2 - 60, 20);
      break;
    case 0:
    case 3:
    case 4:
    case 5:
    case 6:
    case 9:
      paramGraphics.drawImage(paramImage, paramInt1 - i, paramInt2, 20);
      break;
    case 10:
    case 11:
    case 12:
      paramGraphics.drawImage(paramImage, paramInt1 - i, paramInt2 - (this.m_status - 10 + 1) * 15, 20);
      break;
    case 13:
      paramGraphics.drawImage(BubbleSmile.GetImage(), paramInt1 - 104, paramInt2 - 15, 20);
      return;
    case 7:
    case 8:
    case 14:
    }
  }

  public void DrawNormal(Graphics paramGraphics, Image paramImage, int paramInt1, int paramInt2)
  {
    int i = paramGraphics.getClipX();
    int j = paramGraphics.getClipY();
    int k = paramGraphics.getClipWidth();
    int l = paramGraphics.getClipHeight();
    paramGraphics.setClip(paramInt1, paramInt2, 15, 15);
    int i1 = 15 * this.m_num;
    paramGraphics.drawImage(paramImage, paramInt1 - i1, paramInt2, 20);
    paramGraphics.setClip(i, j, k, l);
  }

  public void RandomizeNormalStatus()
  {
    if (this.m_status == 9)
    {
      this.m_status = 0;
    }
    else if (this.m_status == 2)
    {
      this.m_status = 9;
    }
    else if (this.m_status == 1)
    {
      this.m_status = 2;
    }
    else
    {
      if (this.m_status != 0)
        return;
      int i = rand.nextInt();
      if (i < 0)
        i = -i;
      int j = i % 257;
      if (j <= 254)
        return;
      this.m_status = 1;
    }
  }

  void PutSrcPos(byte paramByte1, byte paramByte2)
  {
    this.m_sc = paramByte1;
    this.m_sr = paramByte2;
  }

  byte GetSrcPosCol()
  {
    return this.m_sc;
  }

  byte GetSrcPosRow()
  {
    return this.m_sr;
  }

  void PutDstPos(byte paramByte1, byte paramByte2)
  {
    this.m_dc = paramByte1;
    this.m_dr = paramByte2;
  }

  byte GetDstPosCol()
  {
    return this.m_dc;
  }

  byte GetDstPosRow()
  {
    return this.m_dr;
  }

  byte GetDirection()
  {
    return this.m_direction;
  }

  void PutDirection(byte paramByte)
  {
    this.m_direction = paramByte;
  }

  public int Save(byte[] paramArrayOfByte)
  {
    paramArrayOfByte[0] = this.m_num;
    return 1;
  }

  public void Load(byte[] paramArrayOfByte)
  {
    this.m_num = paramArrayOfByte[0];
    this.m_status = 0;
  }
}