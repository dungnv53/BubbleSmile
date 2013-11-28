import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

class BubbleControllerDestroy extends BubbleController
{
  private static final int MAX_STEP = 5;
  private byte m_step = 0;
  private boolean m_first_flag = true;
  private byte m_repaint_flag = 0;

  public int DelayTime()
  {
    if (this.m_repaint_flag == 1)
    {
      this.m_repaint_flag = 2;
      return 400;
    }
    return 50;
  }

  public void SetRepaintFlag()
  {
    this.m_repaint_flag = 1;
  }

  public boolean IsRepaint()
  {
    return this.m_repaint_flag != 0;
  }

  public void Repaint(BubbleGame paramBubbleGame)
  {
    if (this.m_repaint_flag != 2)
      return;
    paramBubbleGame.repaint();
    this.m_repaint_flag = 0;
  }

  public void FirstTime()
  {
    this.m_first_flag = true;
  }

  public void Init(BubbleGame paramBubbleGame, Image paramImage)
  {
    BubbleController.m_offscreen = paramImage;
    this.m_step = 0;
  }

  public void CreateOffscreen(BubbleGame paramBubbleGame)
  {
    Graphics localGraphics = BubbleController.m_offscreen.getGraphics();
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Image localImage = BubbleSmile.GetImage();
    for (byte b2 = 0; b2 < 9; b2 = (byte)(b2 + 1))
      for (byte b1 = 0; b1 < 7; b1 = (byte)(b1 + 1))
      {
        Bubble localBubble = localBubblePool.GetBubble(b1, b2);
        if (localBubble == null)
          continue;
        if (!localBubble.IsDelete())
          continue;
        int k = paramBubbleGame.Pos2Pixel(b1, b2);
        int i = k & 0xFFFF;
        int j = k >> 16 & 0xFFFF;
        localGraphics.setClip(i, j, 15, 15);
        localGraphics.drawImage(localImage, i - 100, j, 20);
      }
    localGraphics.setClip(0, 0, 128, 128);
  }

  public void KeyPressed(BubbleGame paramBubbleGame, int paramInt)
  {
  }

  public void Final(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    boolean bool = localBubblePool.DeleteBubbles();
    ScorePool localScorePool = paramBubbleGame.GetScorePool();
    localScorePool.UnlockScores();
    if (bool == true)
    {
      BubbleControllerDownAndFill localBubbleControllerDownAndFill = new BubbleControllerDownAndFill();
      localBubbleControllerDownAndFill.Init(BubbleController.m_offscreen);
      super.SetActive(false);
      localBubbleControllerDownAndFill.SetActive(true);
      paramBubbleGame.repaint();
      return;
    }
    super.SetActive(false);
    paramBubbleGame.repaint();
  }

  public void Draw(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    if (this.m_first_flag == true)
    {
      ScorePool localScorePool = paramBubbleGame.GetScorePool();
      localScorePool.IncreaseComboCnt();
      BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
      Checker localChecker = paramBubbleGame.GetChecker();
      boolean bool = localChecker.CheckSame(localBubblePool, paramBubbleGame);
      if (!bool)
      {
        paramBubbleGame.repaint();
        super.SetActive(false);
        return;
      }
      this.m_first_flag = false;
      CreateOffscreen(paramBubbleGame);
      BubbleSmile.PlaySound(2);
    }
    DrawBubbles(BubbleController.m_offscreen.getGraphics(), paramBubbleGame);
    paramGraphics.drawImage(BubbleController.m_offscreen, 0, 0, 20);
    paramBubbleGame.DrawScore(paramGraphics);
    paramBubbleGame.DrawConstraint(paramGraphics);
    paramBubbleGame.DrawCombos(paramGraphics);
    this.m_step = (byte)(this.m_step + 1);
    if (this.m_step >= 5)
      Final(paramGraphics, paramBubbleGame);
    paramBubbleGame.repaint();
  }

  public void DrawBubbles(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Image localImage = paramBubbleGame.GetBubbleImages();
    for (byte b2 = 0; b2 < 9; b2 = (byte)(b2 + 1))
      for (byte b1 = 0; b1 < 7; b1 = (byte)(b1 + 1))
      {
        Bubble localBubble = localBubblePool.GetBubble(b1, b2);
        if (localBubble == null)
          continue;
        int i = paramBubbleGame.Pos2Pixel(b1, b2);
        if (!localBubble.IsDelete())
          continue;
        localBubble.Draw(paramGraphics, localImage, i & 0xFFFF, i >> 16 & 0xFFFF);
        localBubble.NextDeleteStatus();
      }
  }
}