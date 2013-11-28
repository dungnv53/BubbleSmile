import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class BubbleGame extends Canvas
{
  public static final int OFFSET_X_OF_ODD_LINE = 8;
  public static final int OFFSET_Y_OF_ODD_LINE = 5;
  public static final int BUBBLE_IMAGE_W = 15;
  public static final int BUBBLE_IMAGE_H = 15;
  private static final int BUBBLE_OFFSET_X = 5;
  private static final int BUBBLE_OFFSET_Y = 11;
  private static final int GAUGE_LEN = 8;
  private static final String STG = "BUBBLESMILE";
  BubbleSmile m_bs = null;
  private Image m_img_back = null;
  private Image m_img_bubble = null;
  private Image m_img_offscreen = null;
  private BubblePool m_pool = null;
  private Cursor m_cursor = null;
  private Checker m_checker = null;
  private ScorePool m_sp = null;
  private BubbleGameMode m_mode = null;
  private BubbleGame.BubbleGameThread m_thread = null;
  private int m_gauge_pos = 0;
  private boolean m_continue_flag = false;
  private boolean m_first_draw_flag = true;
  private Graphics m_real_g = null;
  private boolean m_key_flag = false;

  public final Image GetOffScreen()
  {
    return this.m_img_offscreen;
  }

  public final Graphics GetRealGraphics()
  {
    return this.m_real_g;
  }

  public BubbleGame(BubbleSmile paramBubbleSmile, BubbleGameMode paramBubbleGameMode, boolean paramBoolean)
  {
    this.m_bs = paramBubbleSmile;
    this.m_mode = paramBubbleGameMode;
    this.m_continue_flag = paramBoolean;
  }

  private void InitImages()
  {
    try
    {
      this.m_img_back = Image.createImage("/back.png");
      this.m_img_bubble = Image.createImage("/bubbles.png");
      this.m_img_offscreen = Image.createImage(128, 128);
    }
    catch (IOException localIOException)
    {
    }
  }

  private void InitBubbles()
  {
    Bubble.InitClass();
    this.m_pool = new BubblePool();
    this.m_pool.InitBubbles();
    Random localRandom = new Random();
    int i = localRandom.nextInt();
    if (i < 0)
      i = -i;
    i %= 10;
    while (i > 0)
    {
      byte b1 = (byte)localRandom.nextInt();
      byte b2 = (byte)localRandom.nextInt();
      if (b1 < 0)
        b1 = (byte)(-b1);
      if (b2 < 0)
        b2 = (byte)(-b2);
      if (b1 > 56)
        b1 = 56;
      if (b2 > 56)
        b2 = 56;
      b2 = (byte)(b2 % 7);
      b1 = (byte)(b1 % 9);
      try
      {
        this.m_pool.PutBubble(b2, b1, null);
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
      }
      --i;
    }
    localRandom = null;
    System.gc();
  }

  private void InitCursor()
  {
    this.m_cursor = new Cursor();
  }

  private void InitThread()
  {
    this.m_thread = new BubbleGame.BubbleGameThread(this);
    this.m_thread.start();
  }

  private void InitController()
  {
    BubbleController.InitClass();
    BubbleController.ClearControllerStack();
    BubbleController.m_mover = new BubbleControllerMove();
    BubbleControllerDownAndFill.InitClass2();
    BubbleControllerNormal localBubbleControllerNormal = new BubbleControllerNormal();
    localBubbleControllerNormal.Init(this);
    localBubbleControllerNormal.SetActive(true);
  }

  private void InitChecker()
  {
    this.m_checker = new Checker();
  }

  private void InitScore()
  {
    this.m_sp = new ScorePool();
  }

  public Image GetOffscreen()
  {
    return this.m_img_offscreen;
  }

  public void paint(Graphics paramGraphics)
  {
    if (this.m_img_offscreen == null)
      return;
    Graphics localGraphics = this.m_img_offscreen.getGraphics();
    if (!IsGaugeEnd())
      return;
    this.m_key_flag = false;
    this.m_real_g = paramGraphics;
    BubbleController localBubbleController = BubbleController.CurrentController();
    if (localBubbleController == null)
      return;
    localBubbleController.Draw(localGraphics, this);
    if (this.m_first_draw_flag == true)
    {
      this.m_first_draw_flag = false;
      BubbleControllerDownAndFill localBubbleControllerDownAndFill = new BubbleControllerDownAndFill();
      BubbleControllerDownAndFill.FirstTime();
      localBubbleControllerDownAndFill.Init(BubbleController.m_offscreen);
      localBubbleControllerDownAndFill.SetActive(true);
      super.repaint();
    }
    if ((this.m_img_offscreen == null) || (paramGraphics == null))
    {
      this.m_real_g = null;
      return;
    }
    paramGraphics.drawImage(this.m_img_offscreen, 0, 0, 20);
    this.m_real_g = null;
  }

  protected void showNotify()
  {
    System.gc();
    InitThread();
  }

  public void Term()
  {
    this.m_pool.TermBubbles();
    this.m_checker.Term();
    this.m_cursor.Term();
    this.m_sp.Term();
    Bubble.TermClass();
    BubbleControllerDownAndFill.TermClass2();
    BubbleController.TermClass();
    this.m_pool = null;
    this.m_checker = null;
    this.m_cursor = null;
    this.m_sp = null;
    this.m_img_offscreen = null;
    this.m_img_back = null;
    this.m_img_bubble = null;
    this.m_bs = null;
    this.m_mode = null;
    this.m_thread = null;
    System.gc();
  }

  public final void DrawBoard(Graphics paramGraphics)
  {
    paramGraphics.drawImage(this.m_img_back, 0, 0, 20);
  }

  public final void DrawConstraint(Graphics paramGraphics)
  {
    this.m_mode.Draw(paramGraphics);
  }

  public final void DrawCombos(Graphics paramGraphics)
  {
    this.m_sp.DrawScores(paramGraphics);
  }

  public final void DrawCursor(Graphics paramGraphics)
  {
    this.m_cursor.Draw(paramGraphics, this);
  }

  public void DrawScore(Graphics paramGraphics)
  {
    int i = this.m_sp.GetCurrentScore();
    int j = i;
    int k = 0;
    while (j > 0)
    {
      ++k;
      j /= 10;
    }
    BubbleSmile.DrawNumber(paramGraphics, 127 - k * 7, 0, i, 4);
    this.m_sp.NextCurrentScore();
  }

  public final int Pos2Pixel(byte paramByte1, byte paramByte2)
  {
    int i = (short)(16 * paramByte1);
    int j = (paramByte2 % 2 != 0) ? 1 : 0;
    if (j != 0)
      i = (short)(i + 8);
    int k = (short)(15 * paramByte2);
    k = (short)(k - paramByte2 / 2 * 5);
    if (j != 0)
      k = (short)(k - 2);
    i = (short)(i + 5);
    k = (short)(k + 11);
    return i | k << 16;
  }

  public void StopThread()
  {
    if (this.m_thread == null)
      return;
    this.m_thread.SetExitFlag(true);
    try
    {
      this.m_thread.join();
    }
    catch (InterruptedException localInterruptedException)
    {
    }
    this.m_thread = null;
  }

  public void keyPressed(int paramInt)
  {
    if ((paramInt == 35) || (paramInt == -8) || (paramInt == -7))
    {
      try
      {
        StopThread();
        DeleteRS();
        Save();
        BubbleSmile localBubbleSmile = this.m_bs;
        Term();
        localBubbleSmile.SetCurrent(new BubbleLogo(localBubbleSmile));
      }
      catch (NullPointerException localNullPointerException)
      {
      }
      return;
    }
    if (this.m_key_flag == true)
      return;
    this.m_key_flag = true;
    BubbleController localBubbleController = BubbleController.CurrentController();
    if (localBubbleController != null)
      localBubbleController.KeyPressed(this, paramInt);
    else
      super.repaint();
    super.serviceRepaints();
  }

  public final Checker GetChecker()
  {
    return this.m_checker;
  }

  public final BubbleGameMode GetBubbleGameMode()
  {
    return this.m_mode;
  }

  public final ScorePool GetScorePool()
  {
    return this.m_sp;
  }

  public final Cursor GetCursor()
  {
    return this.m_cursor;
  }

  public final Image GetBubbleImages()
  {
    return this.m_img_bubble;
  }

  public final BubblePool GetBubblePool()
  {
    return this.m_pool;
  }

  public final void StepGauge()
  {
    this.m_gauge_pos += 1;
    super.repaint();
  }

  public final boolean IsGaugeEnd()
  {
    return this.m_gauge_pos >= 8;
  }

  public final void EndGauge()
  {
    this.m_gauge_pos = 8;
  }

  public void ChangeDisplayToHighscore()
  {
    BubbleSmile localBubbleSmile = this.m_bs;
    int i = this.m_sp.GetTotalScore();
    StopThread();
    Term();
    System.gc();
    BubbleHighScore localBubbleHighScore = new BubbleHighScore(localBubbleSmile, i);
    localBubbleSmile.SetCurrent(localBubbleHighScore);
  }

  public static final boolean IsSaved()
  {
    RecordStore localRecordStore = null;
    try
    {
      localRecordStore = RecordStore.openRecordStore("BUBBLESMILE", false);
      int i = 1;
      byte[] arrayOfByte = new byte[localRecordStore.getRecordSize(i)];
      localRecordStore.getRecord(i, arrayOfByte, 0);
      if ((arrayOfByte[0] != 1) || (arrayOfByte[1] != 1))
        return false;
      localRecordStore.closeRecordStore();
    }
    catch (InvalidRecordIDException localInvalidRecordIDException)
    {
      return false;
    }
    catch (RecordStoreNotOpenException localRecordStoreNotOpenException)
    {
      return false;
    }
    catch (RecordStoreException localRecordStoreException)
    {
      return false;
    }
    return true;
  }

  public boolean Load()
  {
    RecordStore localRecordStore = null;
    try
    {
      localRecordStore = RecordStore.openRecordStore("BUBBLESMILE", false);
      int i = 1;
      byte[] arrayOfByte = new byte[localRecordStore.getRecordSize(i)];
      localRecordStore.getRecord(i, arrayOfByte, 0);
      if ((arrayOfByte[0] != 1) || (arrayOfByte[1] != 1))
        return false;
      i = 2;
      arrayOfByte = new byte[localRecordStore.getRecordSize(i)];
      localRecordStore.getRecord(i, arrayOfByte, 0);
      this.m_pool.Load(arrayOfByte);
      i = 3;
      arrayOfByte = new byte[localRecordStore.getRecordSize(i)];
      localRecordStore.getRecord(i, arrayOfByte, 0);
      this.m_cursor.Load(arrayOfByte);
      i = 4;
      arrayOfByte = new byte[localRecordStore.getRecordSize(i)];
      localRecordStore.getRecord(i, arrayOfByte, 0);
      this.m_sp.Load(arrayOfByte);
      i = 5;
      arrayOfByte = new byte[localRecordStore.getRecordSize(i)];
      localRecordStore.getRecord(i, arrayOfByte, 0);
      this.m_mode.Load(arrayOfByte);
      localRecordStore.closeRecordStore();
    }
    catch (InvalidRecordIDException localInvalidRecordIDException)
    {
      DeleteRS();
      return false;
    }
    catch (RecordStoreException localRecordStoreException)
    {
      DeleteRS();
      return false;
    }
    return true;
  }

  public void DeleteRS()
  {
    try
    {
      RecordStore.deleteRecordStore("BUBBLESMILE");
    }
    catch (Exception localException)
    {
    }
  }

  public boolean Save()
  {
    RecordStore localRecordStore = null;
    try
    {
      localRecordStore = RecordStore.openRecordStore("BUBBLESMILE", true);
      byte[] arrayOfByte = new byte[2];
      arrayOfByte[0] = 1;
      arrayOfByte[1] = 1;
      localRecordStore.addRecord(arrayOfByte, 0, arrayOfByte.length);
      arrayOfByte = new byte[this.m_pool.GetSaveSize()];
      this.m_pool.Save(arrayOfByte);
      localRecordStore.addRecord(arrayOfByte, 0, arrayOfByte.length);
      arrayOfByte = new byte[this.m_cursor.GetSaveSize()];
      this.m_cursor.Save(arrayOfByte);
      localRecordStore.addRecord(arrayOfByte, 0, arrayOfByte.length);
      arrayOfByte = new byte[this.m_sp.GetSaveSize()];
      this.m_sp.Save(arrayOfByte);
      localRecordStore.addRecord(arrayOfByte, 0, arrayOfByte.length);
      arrayOfByte = new byte[this.m_mode.GetSaveSize()];
      this.m_mode.Save(arrayOfByte);
      localRecordStore.addRecord(arrayOfByte, 0, arrayOfByte.length);
      localRecordStore.closeRecordStore();
    }
    catch (RecordStoreException localRecordStoreException)
    {
      DeleteRS();
    }
    return true;
  }

  class BubbleGameThread extends Thread
  {
    private static final int DELAY_TIME = 300;
    private BubbleGame m_game = null;
    private boolean m_exit = false;

    public BubbleGameThread(BubbleGame arg2)
    {
      Object localObject;
      this.m_game = localObject;
    }

    public void run()
    {
      BubbleGame.this.StepGauge();
      BubbleGame.this.InitImages();
      BubbleGame.this.StepGauge();
      BubbleGame.this.InitBubbles();
      BubbleGame.this.StepGauge();
      BubbleGame.this.InitCursor();
      BubbleGame.this.StepGauge();
      BubbleGame.this.InitChecker();
      BubbleGame.this.StepGauge();
      BubbleGame.this.InitController();
      BubbleGame.this.StepGauge();
      BubbleGame.this.InitScore();
      if (BubbleGame.this.m_continue_flag == true)
      {
        BubbleGame.this.StepGauge();
        if (true == BubbleGame.this.Load())
        {
          BubbleController localBubbleController = BubbleController.GetNormal();
          if (localBubbleController != null)
          {
            localObject = (BubbleControllerNormal)localBubbleController;
            ((BubbleControllerNormal)localObject).Init(this.m_game);
            ((BubbleControllerNormal)localObject).AdjustBonusFlagByScore(this.m_game.GetScorePool().GetTotalScore());
          }
        }
        else
        {
          BubbleGame.access$602(BubbleGame.this, false);
        }
      }
      BubbleGame.this.DeleteRS();
      BubbleGame.this.EndGauge();
      while (!this.m_exit)
      {
        int i;
        try
        {
          localObject = BubbleController.CurrentController();
          if (localObject == null)
          {
            Thread.yield();
            break label249:
          }
          if (((BubbleController)localObject).IsRepaint() == true)
            ((BubbleController)localObject).Repaint(this.m_game);
          localObject = BubbleController.CurrentController();
          if (localObject == null)
            break label249;
          i = ((BubbleController)localObject).DelayTimeEntry();
        }
        catch (NullPointerException localNullPointerException)
        {
          i = 10;
        }
        label249: 
        try
        {
          if (i == 0)
            Thread.yield();
          else
            Thread.sleep(i);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
      Object localObject = null;
      this.m_game = null;
    }

    public void SetExitFlag(boolean paramBoolean)
    {
      this.m_exit = paramBoolean;
    }
  }
}