import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

class BubbleControllerRotator extends BubbleController
{
  private boolean m_first = true;
  private boolean m_dir = true;
  private short m_p1 = 0;
  private short m_p2 = 0;
  private short m_p3 = 0;

  public void Init(BubbleGame paramBubbleGame, boolean paramBoolean, Image paramImage)
  {
    this.m_first = true;
    this.m_dir = paramBoolean;
    BubbleController.m_offscreen = paramImage;
  }

  public void First(BubbleGame paramBubbleGame)
  {
    Graphics localGraphics = BubbleController.m_offscreen.getGraphics();
    EraseBubbles(localGraphics, paramBubbleGame);
    Cursor localCursor = paramBubbleGame.GetCursor();
    this.m_p1 = localCursor.GetAt(0);
    this.m_p2 = localCursor.GetAt(1);
    this.m_p3 = localCursor.GetAt(2);
    localCursor.Rotation(paramBubbleGame, this.m_dir);
  }

  public void Final(BubbleGame paramBubbleGame)
  {
    Graphics localGraphics = BubbleController.m_offscreen.getGraphics();
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Cursor localCursor = paramBubbleGame.GetCursor();
    Image localImage = paramBubbleGame.GetBubbleImages();
    int i = 0;
    for (int j = 0; j < 3; ++j)
    {
      switch (j)
      {
      case 0:
        i = this.m_p1;
        break;
      case 1:
        i = this.m_p2;
        break;
      case 2:
        i = this.m_p3;
      }
      byte b1 = (byte)(i & 0xFF);
      byte b2 = (byte)(i >> 8 & 0xFF);
      Bubble localBubble = localBubblePool.GetBubble(b1, b2);
      int k = paramBubbleGame.Pos2Pixel(b1, b2);
      localBubble.DrawNormal(localGraphics, localImage, k & 0xFFFF, k >> 16 & 0xFFFF);
    }
  }

  public void Draw(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    if (this.m_first)
    {
      this.m_first = false;
      First(paramBubbleGame);
      paramGraphics.drawImage(BubbleController.m_offscreen, 0, 0, 20);
      DrawBubbles(paramGraphics, paramBubbleGame);
      BubbleSmile.PlaySound(3);
    }
    else
    {
      BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
      localBubblePool.MoveBubble();
      Final(paramBubbleGame);
      paramGraphics.drawImage(BubbleController.m_offscreen, 0, 0, 20);
      BubbleControllerDestroy localBubbleControllerDestroy = new BubbleControllerDestroy();
      BubbleControllerDownAndFill.FirstTime();
      localBubbleControllerDestroy.Init(paramBubbleGame, BubbleController.m_offscreen);
      super.SetActive(false);
      localBubbleControllerDestroy.SetActive(true);
    }
    paramBubbleGame.DrawScore(paramGraphics);
    paramBubbleGame.DrawConstraint(paramGraphics);
    paramBubbleGame.DrawCombos(paramGraphics);
    paramBubbleGame.repaint();
  }

  public int DelayTime()
  {
    return 50;
  }

  public void EraseBubbles(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    Cursor localCursor = paramBubbleGame.GetCursor();
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Image localImage = BubbleSmile.GetImage();
    for (int l = 0; l < 3; ++l)
    {
      int i = localCursor.GetAt(l);
      byte b1 = (byte)(i & 0xFF);
      byte b2 = (byte)(i >> 8 & 0xFF);
      Bubble localBubble = localBubblePool.GetBubble(b1, b2);
      int i1 = paramBubbleGame.Pos2Pixel(b1, b2);
      int j = i1 & 0xFFFF;
      int k = i1 >> 16 & 0xFFFF;
      paramGraphics.setClip(j, k, 15, 15);
      paramGraphics.drawImage(localImage, j - 100, k, 20);
    }
  }

  public void DrawBubbles(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Image localImage = paramBubbleGame.GetBubbleImages();
    for (byte b1 = 0; b1 < 7; b1 = (byte)(b1 + 1))
      for (byte b2 = 8; b2 >= 0; b2 = (byte)(b2 - 1))
      {
        Bubble localBubble = localBubblePool.GetBubble(b1, b2);
        if (localBubble == null)
          continue;
        if (!localBubble.IsMoving())
          continue;
        int i = paramBubbleGame.Pos2Pixel(b1, b2);
        i = BubbleControllerMove.ApplyDirectionWithStep(localBubble, i);
        localBubble.NextMovingStatus();
        localBubble.Draw(paramGraphics, localImage, i & 0xFFFF, i >> 16 & 0xFFFF);
      }
  }
}