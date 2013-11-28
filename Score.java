import javax.microedition.lcdui.Graphics;

public class Score
{
  private static final byte MAX_COUNT = 10;
  private short m_x;
  private short m_y;
  private boolean m_lock = true;
  private short m_score;
  private byte m_combo;
  private byte m_counter = 0;

  public void CalcScore(byte paramByte1, byte paramByte2)
  {
    this.m_score = (short)((paramByte1 + 1) * 5 * paramByte2);
    this.m_combo = paramByte2;
  }

  public short GetScore()
  {
    return this.m_score;
  }

  public void PutPos(short paramShort1, short paramShort2)
  {
    this.m_x = paramShort1;
    this.m_y = paramShort2;
  }

  public void Draw(Graphics paramGraphics)
  {
    BubbleSmile.DrawNumber(paramGraphics, this.m_x, this.m_y, this.m_score, 0);
    int i = this.m_x;
    int j = this.m_y + 15;
    if (this.m_combo <= 1)
      return;
    paramGraphics.setClip(i, j, 33, 10);
    paramGraphics.drawImage(BubbleSmile.GetImage(), i, j - 15, 20);
    BubbleSmile.DrawNumber(paramGraphics, i += 33, j, this.m_combo - 1, 3);
    paramGraphics.setClip(0, 0, 128, 128);
  }

  public final void MoveNext()
  {
    this.m_y = (short)(this.m_y - 1);
    this.m_counter = (byte)(this.m_counter + 1);
  }

  final boolean IsEnd()
  {
    return this.m_counter >= 10;
  }

  public final boolean IsLocked()
  {
    return this.m_lock;
  }

  public final void Unlock()
  {
    this.m_lock = false;
  }
}