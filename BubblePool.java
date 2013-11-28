import java.util.Vector;

public class BubblePool
{
  public static final byte POOL_SIZE_X = 7;
  public static final byte POOL_SIZE_Y = 9;
  public static final byte IT_NORMAL = 0;
  public static final byte IT_LINE = 1;
  public static final byte IT_RLDIAGONAL = 2;
  public static final byte IT_LRDIAGONAL = 3;
  private Bubble[][] m_pool = new Bubble[9][7];
  private byte m_ittype;
  private byte m_c;
  private byte m_r;
  private Vector m_move_pool = null;

  public final Bubble GetBubble(byte paramByte1, byte paramByte2)
  {
    return this.m_pool[paramByte2][paramByte1];
  }

  public final void PutBubble(byte paramByte1, byte paramByte2, Bubble paramBubble)
  {
    this.m_pool[paramByte2][paramByte1] = paramBubble;
  }

  public void InitBubbles()
  {
    for (int i = 0; i < 9; i = (byte)(i + 1))
      for (int j = 0; j < 7; j = (byte)(j + 1))
      {
        if (this.m_pool[i][j] == null)
          this.m_pool[i][j] = new Bubble();
        this.m_pool[i][j].Randomize();
      }
  }

  public void TermBubbles()
  {
    for (int i = 0; i < 9; i = (byte)(i + 1))
      for (int j = 0; j < 7; j = (byte)(j + 1))
        this.m_pool[i][j] = null;
    this.m_move_pool.removeAllElements();
    this.m_move_pool = null;
  }

  public final void Begin(byte paramByte1, byte paramByte2, byte paramByte3)
  {
    this.m_ittype = paramByte1;
    switch (this.m_ittype)
    {
    case 0:
      this.m_r = 0;
      this.m_c = 0;
      break;
    case 1:
      this.m_r = paramByte3;
      this.m_c = 0;
      break;
    case 2:
    case 3:
      this.m_r = paramByte3;
      this.m_c = paramByte2;
    }
  }

  public final void Next()
  {
    switch (this.m_ittype)
    {
    case 0:
      this.m_c = (byte)(this.m_c + 1);
      if (this.m_c < 7)
        return;
      this.m_c = 0;
      this.m_r = (byte)(this.m_r + 1);
      break;
    case 1:
      this.m_c = (byte)(this.m_c + 1);
      break;
    case 3:
      if (this.m_r % 2 != 0)
        this.m_c = (byte)(this.m_c + 1);
      this.m_r = (byte)(this.m_r + 1);
      break;
    case 2:
      if (this.m_r % 2 == 0)
        this.m_c = (byte)(this.m_c - 1);
      this.m_r = (byte)(this.m_r + 1);
    }
  }

  public final boolean IsDone()
  {
    switch (this.m_ittype)
    {
    case 0:
      return this.m_r >= 9;
    case 1:
      return this.m_c >= 7;
    case 2:
    case 3:
      return (this.m_r >= 9) || (this.m_c >= 7) || (this.m_c < 0);
    }
    return true;
  }

  public final Bubble CurrentItem()
  {
    return this.m_pool[this.m_r][this.m_c];
  }

  public final byte CurrentItemCol()
  {
    return this.m_c;
  }

  public final byte CurrentItemRow()
  {
    return this.m_r;
  }

  public void MoveBubble()
  {
    Bubble localBubble = null;
    this.m_move_pool.removeAllElements();
    for (int i = 0; i < 7; i = (byte)(i + 1))
      for (int j = 0; j < 9; j = (byte)(j + 1))
      {
        localBubble = this.m_pool[j][i];
        if (localBubble == null)
          continue;
        if (!localBubble.IsMoving())
          continue;
        this.m_move_pool.addElement(localBubble);
        this.m_pool[j][i] = null;
      }
    int k = this.m_move_pool.size();
    for (int l = 0; l < k; ++l)
    {
      localBubble = (Bubble)this.m_move_pool.elementAt(l);
      this.m_pool[localBubble.m_dr][localBubble.m_dc] = localBubble;
      localBubble.m_status = 0;
    }
  }

  public final boolean DeleteBubbles()
  {
    int k = 0;
    for (int i = 0; i < 9; i = (byte)(i + 1))
      for (int j = 0; j < 7; j = (byte)(j + 1))
      {
        if (true != this.m_pool[i][j].IsDelete())
          continue;
        this.m_pool[i][j] = null;
        k = 1;
      }
    return k;
  }

  public final boolean DownBubbles(Vector paramVector)
  {
    int j = 0;
    int k = 0;
    int i1 = 0;
    for (byte b1 = 0; b1 < 7; b1 = (byte)(b1 + 1))
    {
      j = 0;
      int l = 1;
      for (int i = 8; i > 0; i = (byte)(i - 1))
      {
        if (null != this.m_pool[i][b1])
          continue;
        for (byte b2 = (byte)(i - 1); (b2 >= 0) && (this.m_pool[b2][b1] == null); b2 = (byte)(b2 - 1));
        if (b2 < 0)
          continue;
        Bubble localBubble = this.m_pool[b2][b1];
        if (localBubble == null)
          continue;
        if (l == 1)
        {
          paramVector.addElement(localBubble);
          l = 0;
        }
        j = 1;
        localBubble.PutStatus(3);
        localBubble.PutSrcPos(b1, b2);
        localBubble.PutDstPos(b1, (byte)(b2 + 1));
        if (b2 % 2 == 0)
          localBubble.PutDirection(2);
        else
          localBubble.PutDirection(-1);
        i = (byte)(b2 + 1);
      }
      k |= j;
    }
    return k;
  }

  public boolean IsFull()
  {
    for (int i = 0; i < 9; i = (byte)(i + 1))
      for (int j = 0; j < 7; j = (byte)(j + 1))
        if (this.m_pool[i][j] == null)
          return false;
    return true;
  }

  public boolean FillFirstLine(Vector paramVector)
  {
    Bubble localBubble = null;
    int i = 0;
    for (byte b = 0; b < 7; b = (byte)(b + 1))
    {
      if (this.m_pool[0][b] != null)
        continue;
      localBubble = new Bubble();
      localBubble.Randomize();
      this.m_pool[0][b] = localBubble;
      localBubble.PutSrcPos(b, 0);
      paramVector.addElement(localBubble);
      i = 1;
    }
    return i;
  }

  public int GetSaveSize()
  {
    return 63;
  }

  public int Save(byte[] paramArrayOfByte)
  {
    Bubble localBubble = null;
    int k = 0;
    byte[] arrayOfByte = new byte[10];
    for (int j = 0; j < 9; j = (byte)(j + 1))
      for (int i = 0; i < 7; i = (byte)(i + 1))
      {
        localBubble = this.m_pool[j][i];
        if (null == localBubble)
          arrayOfByte[0] = -1;
        else
          localBubble.Save(arrayOfByte);
        paramArrayOfByte[(k++)] = arrayOfByte[0];
      }
    return k;
  }

  public void Load(byte[] paramArrayOfByte)
  {
    Bubble localBubble = null;
    int k = 0;
    byte[] arrayOfByte = new byte[10];
    for (int j = 0; j < 9; j = (byte)(j + 1))
      for (int i = 0; i < 7; i = (byte)(i + 1))
      {
        arrayOfByte[0] = paramArrayOfByte[(k++)];
        if ((arrayOfByte[0] < 0) || (arrayOfByte[0] > 6))
        {
          this.m_pool[j][i] = null;
        }
        else
        {
          localBubble = this.m_pool[j][i];
          if (localBubble == null)
          {
            localBubble = new Bubble();
            this.m_pool[j][i] = localBubble;
          }
          localBubble.Load(arrayOfByte);
        }
      }
  }
}