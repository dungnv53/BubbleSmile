public class Checker
{
  private static final int MAX_POOL_SIZE = 9;
  private static final int MINIMUM_BLOCK_COUNT = 3;
  private short[] m_pospool = new short[9];
  private byte m_index = -1;
  private byte m_num = -1;
  private byte m_checked_total_bubbles = 0;
  private byte m_min_c;
  private byte m_max_c;
  private byte m_min_r;
  private byte m_max_r;
  private boolean m_bonus_mode = false;

  public final void Clear()
  {
    this.m_index = -1;
    this.m_num = -1;
  }

  public final void Term()
  {
    this.m_pospool = null;
  }

  public final void ClearScoreRelatedVariables()
  {
    this.m_checked_total_bubbles = 0;
    this.m_min_c = 100;
    this.m_max_c = -1;
    this.m_min_r = 100;
    this.m_max_r = -1;
  }

  public final void Add(byte paramByte1, byte paramByte2)
  {
    this.m_index = (byte)(this.m_index + 1);
    int i = (short)(int)(paramByte1 | paramByte2 << 8);
    this.m_pospool[this.m_index] = i;
  }

  public final short GetCount()
  {
    return (short)(this.m_index + 1);
  }

  public final boolean CheckSame(BubblePool paramBubblePool, BubbleGame paramBubbleGame)
  {
    boolean bool = false;
    Clear();
    ClearScoreRelatedVariables();
    bool = CheckSameByLine(paramBubblePool, paramBubbleGame);
    bool |= CheckSameByLRDiagonal(paramBubblePool, paramBubbleGame);
    bool |= CheckSameByRLDiagonal(paramBubblePool, paramBubbleGame);
    if (bool == true)
    {
      int i = paramBubbleGame.Pos2Pixel((byte)((this.m_max_c + this.m_min_c) / 2), (byte)((this.m_max_r + this.m_min_r) / 2));
      ScorePool localScorePool = paramBubbleGame.GetScorePool();
      localScorePool.AddScore(this.m_checked_total_bubbles, (short)(i & 0xFFFF), (short)(i >> 16 & 0xFFFF));
      if (this.m_checked_total_bubbles >= 3)
      {
        BubbleGameMode localBubbleGameMode = paramBubbleGame.GetBubbleGameMode();
        localBubbleGameMode.WeakenConstraint(this.m_checked_total_bubbles);
      }
    }
    this.m_bonus_mode = false;
    return bool;
  }

  private final boolean CheckSameByLine(BubblePool paramBubblePool, BubbleGame paramBubbleGame)
  {
    boolean bool = false;
    for (byte b = 0; b < 9; b = (byte)(b + 1))
      bool |= CheckSameByIterator(paramBubblePool, paramBubbleGame, 1, 0, b);
    return bool;
  }

  private final boolean CheckSameByLRDiagonal(BubblePool paramBubblePool, BubbleGame paramBubbleGame)
  {
    boolean bool = false;
    for (byte b1 = 0; b1 < 7; b1 = (byte)(b1 + 1))
      bool |= CheckSameByIterator(paramBubblePool, paramBubbleGame, 3, b1, 0);
    for (byte b2 = 2; b2 < 9; b2 = (byte)(b2 + 2))
      bool |= CheckSameByIterator(paramBubblePool, paramBubbleGame, 3, 0, b2);
    return bool;
  }

  private final boolean CheckSameByRLDiagonal(BubblePool paramBubblePool, BubbleGame paramBubbleGame)
  {
    boolean bool = false;
    for (byte b1 = 0; b1 < 7; b1 = (byte)(b1 + 1))
      bool |= CheckSameByIterator(paramBubblePool, paramBubbleGame, 2, b1, 0);
    byte b2 = 6;
    for (byte b3 = 1; b3 < 9; b3 = (byte)(b3 + 2))
      bool |= CheckSameByIterator(paramBubblePool, paramBubbleGame, 2, b2, b3);
    return bool;
  }

  private final boolean CheckSameByIterator(BubblePool paramBubblePool, BubbleGame paramBubbleGame, byte paramByte1, byte paramByte2, byte paramByte3)
  {
    int i = 0;
    int j = 0;
    int k = -1;
    paramBubblePool.Begin(paramByte1, paramByte2, paramByte3);
    while (!paramBubblePool.IsDone())
    {
      Bubble localBubble = paramBubblePool.CurrentItem();
      if (localBubble == null)
      {
        if (GetCount() >= 3)
        {
          ApplyCheckedStatus(paramBubblePool, paramBubbleGame);
          j = 1;
        }
        Clear();
      }
      else
      {
        k = localBubble.GetNumber();
        if (this.m_bonus_mode == true)
          k = (byte)(k / 2);
        if (k != this.m_num)
        {
          if (GetCount() >= 3)
          {
            ApplyCheckedStatus(paramBubblePool, paramBubbleGame);
            j = 1;
          }
          else
          {
            Clear();
          }
          this.m_num = k;
        }
        Add(paramBubblePool.CurrentItemCol(), paramBubblePool.CurrentItemRow());
      }
      paramBubblePool.Next();
    }
    if (GetCount() >= 3)
    {
      ApplyCheckedStatus(paramBubblePool, paramBubbleGame);
      j = 1;
    }
    Clear();
    return j;
  }

  private final void ApplyCheckedStatus(BubblePool paramBubblePool, BubbleGame paramBubbleGame)
  {
    byte b1 = 0;
    byte b2 = 0;
    this.m_checked_total_bubbles = (byte)(this.m_checked_total_bubbles + (this.m_index + 1));
    while (this.m_index >= 0)
    {
      int i = this.m_pospool[this.m_index];
      b1 = (byte)(i & 0xFF);
      b2 = (byte)(i >> 8 & 0xFF);
      if (b1 > this.m_max_c)
        this.m_max_c = b1;
      if (b1 < this.m_min_c)
        this.m_min_c = b1;
      if (b2 > this.m_max_r)
        this.m_max_r = b2;
      if (b2 < this.m_min_r)
        this.m_min_r = b2;
      Bubble localBubble = paramBubblePool.GetBubble(b1, b2);
      localBubble.PutStatus(10);
      this.m_index = (byte)(this.m_index - 1);
    }
  }

  public final void SetBonusMode()
  {
    this.m_bonus_mode = true;
  }
}