import javax.microedition.lcdui.Graphics;

class BubbleGameMode
{
  public static final byte MODE_TIME = 1;
  public static final byte MODE_SKILL = 2;
  private static final short BAR_WIDTH = 66;
  private static final short BAR_HEIGHT = 2;
  private static final short BAR_OFFSET_X = 6;
  private static final short BAR_OFFSET_Y = 3;
  private static final int BAR_COLOR_0 = 65280;
  private static final int BAR_COLOR_1 = 16776960;
  private static final int BAR_COLOR_2 = 16711680;
  private static final short MAX_VALUE = 90;
  private static final short MID_VALUE = 40;
  private static final short RISK_VALUE = 20;
  private static final byte TIME_SPEED = 1;
  private byte m_mode = 0;
  private int m_cur = 0;
  private int m_step = 0;

  public void InitMode(byte paramByte)
  {
    this.m_mode = paramByte;
    if (this.m_mode == 1)
      this.m_cur = 90;
    else
      this.m_cur = 27;
  }

  public boolean IsTimedMode()
  {
    return this.m_mode == 1;
  }

  public boolean IsSkilledMode()
  {
    return this.m_mode == 2;
  }

  public boolean IsValid()
  {
    return this.m_cur > 0;
  }

  public void MoveTimer()
  {
    this.m_step += 1;
    if (this.m_step < 1)
      return;
    this.m_step = 0;
    this.m_cur -= 1;
  }

  public void IncreaseTimer()
  {
    this.m_step = 0;
    this.m_cur += 1;
    if (this.m_cur < 90)
      return;
    this.m_cur = 90;
  }

  public void DecreaseRotation()
  {
    this.m_cur -= 1;
  }

  public void IncreaseRotation()
  {
    this.m_cur += 1;
    if (this.m_cur < 90)
      return;
    this.m_cur = 90;
  }

  public void WeakenConstraint(byte paramByte)
  {
    switch (this.m_mode)
    {
    case 1:
      IncreaseTimer();
      break;
    case 2:
      if (paramByte < 5)
        return;
      IncreaseRotation();
    }
  }

  public void Draw(Graphics paramGraphics)
  {
    int i = paramGraphics.getColor();
    int k = (short)(66 * this.m_cur / 90);
    int j;
    if (k > 40)
      j = 65280;
    else if (k > 20)
      j = 16776960;
    else
      j = 16711680;
    paramGraphics.setColor(j);
    paramGraphics.fillRect(6, 3, k, 2);
    paramGraphics.setColor(i);
  }

  public int GetSaveSize()
  {
    return 5;
  }

  public int Save(byte[] paramArrayOfByte)
  {
    paramArrayOfByte[0] = this.m_mode;
    paramArrayOfByte[1] = (byte)(this.m_cur & 0xFF);
    paramArrayOfByte[2] = (byte)(this.m_cur >> 8 & 0xFF);
    paramArrayOfByte[3] = (byte)(this.m_cur >> 16 & 0xFF);
    paramArrayOfByte[4] = (byte)(this.m_cur >> 24 & 0xFF);
    return 5;
  }

  public void Load(byte[] paramArrayOfByte)
  {
    this.m_mode = paramArrayOfByte[0];
    int i = 0;
    i = paramArrayOfByte[1];
    i |= paramArrayOfByte[2] << 8;
    i |= paramArrayOfByte[3] << 16;
    i |= paramArrayOfByte[4] << 24;
    this.m_cur = i;
  }
}