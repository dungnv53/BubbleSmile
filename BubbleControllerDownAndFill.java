import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

class BubbleControllerDownAndFill extends BubbleController
{
  private static boolean m_first_time = true;
  private static Vector m_pool = null;
  private static Vector m_pool_fst_line = null;
  private static final byte S_FIRST = 1;
  private static final byte S_DOWN = 2;
  private byte m_status = 1;

  public static void FirstTime()
  {
    m_first_time = true;
  }

  public static void InitClass2()
  {
    m_pool = new Vector();
    m_pool_fst_line = new Vector();
  }

  public static void TermClass2()
  {
    m_pool.removeAllElements();
    m_pool_fst_line.removeAllElements();
    m_pool = null;
    m_pool_fst_line = null;
  }

  public void Init(Image paramImage)
  {
    BubbleController.m_offscreen = paramImage;
    this.m_status = 1;
  }

  public void Final(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    BubbleControllerDestroy localBubbleControllerDestroy = new BubbleControllerDestroy();
    localBubbleControllerDestroy.Init(paramBubbleGame, BubbleController.m_offscreen);
    super.SetActive(false);
    localBubbleControllerDestroy.SetActive(true);
    paramBubbleGame.repaint();
  }

  public int DelayTime()
  {
    return 50;
  }

  public void Draw(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Graphics localGraphics = paramBubbleGame.GetRealGraphics();
    Image localImage = paramBubbleGame.GetOffScreen();
    if (this.m_status == 2)
    {
      m_pool_fst_line.removeAllElements();
      if (false == localBubblePool.FillFirstLine(m_pool_fst_line))
        if (localBubblePool.IsFull() == true)
        {
          AddBubblesToOffscreen(paramBubbleGame);
          paramGraphics.drawImage(BubbleController.m_offscreen, 0, 0, 20);
          paramBubbleGame.DrawScore(paramGraphics);
          paramBubbleGame.DrawConstraint(paramGraphics);
          paramBubbleGame.DrawCombos(paramGraphics);
          Final(paramGraphics, paramBubbleGame);
          return;
        }
      else
        DrawFirstLine(paramBubbleGame);
      BubbleSmile.PlaySound(1);
      AddBubblesToOffscreen(paramBubbleGame);
      this.m_status = 1;
      m_pool.removeAllElements();
    }
    m_pool.removeAllElements();
    if (localBubblePool.DownBubbles(m_pool) == true)
    {
      CreateOffscreen(paramBubbleGame);
      BubbleControllerMove localBubbleControllerMove = (BubbleControllerMove)BubbleController.m_mover;
      paramGraphics.drawImage(BubbleController.m_offscreen, 0, 0, 20);
      paramBubbleGame.DrawScore(paramGraphics);
      paramBubbleGame.DrawConstraint(paramGraphics);
      localBubbleControllerMove.DrawBubbles(paramGraphics, paramBubbleGame, false);
      localBubblePool.MoveBubble();
      paramBubbleGame.DrawCombos(paramGraphics);
    }
    this.m_status = 2;
    localGraphics.drawImage(localImage, 0, 0, 20);
    paramBubbleGame.repaint();
  }

  public void CreateOffscreen(BubbleGame paramBubbleGame)
  {
    Graphics localGraphics = BubbleController.m_offscreen.getGraphics();
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Image localImage = BubbleSmile.GetImage();
    for (byte b1 = 0; b1 < 7; b1 = (byte)(b1 + 1))
      for (byte b2 = 8; b2 >= 0; b2 = (byte)(b2 - 1))
      {
        Bubble localBubble = localBubblePool.GetBubble(b1, b2);
        if ((localBubble != null) && (!localBubble.IsMoving()))
          continue;
        int k = paramBubbleGame.Pos2Pixel(b1, b2);
        int i = k & 0xFFFF;
        int j = k >> 16 & 0xFFFF;
        localGraphics.setClip(i, j, 15, 15);
        localGraphics.drawImage(localImage, i - 100, j, 20);
      }
    localGraphics.setClip(0, 0, 128, 128);
  }

  public void AddBubblesToOffscreen(BubbleGame paramBubbleGame)
  {
    Graphics localGraphics = BubbleController.m_offscreen.getGraphics();
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Image localImage = paramBubbleGame.GetBubbleImages();
    int j = m_pool.size();
    for (int k = 0; k < j; ++k)
    {
      Bubble localBubble = (Bubble)m_pool.elementAt(k);
      int i = paramBubbleGame.Pos2Pixel(localBubble.GetDstPosCol(), localBubble.GetDstPosRow());
      localBubble.Draw(localGraphics, localImage, i & 0xFFFF, i >> 16 & 0xFFFF);
    }
  }

  public void DrawFirstLine(BubbleGame paramBubbleGame)
  {
    Graphics localGraphics = BubbleController.m_offscreen.getGraphics();
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Image localImage = paramBubbleGame.GetBubbleImages();
    int j = m_pool_fst_line.size();
    for (int k = 0; k < j; ++k)
    {
      Bubble localBubble = (Bubble)m_pool_fst_line.elementAt(k);
      int i = paramBubbleGame.Pos2Pixel(localBubble.GetSrcPosCol(), localBubble.GetSrcPosRow());
      localBubble.Draw(localGraphics, localImage, i & 0xFFFF, i >> 16 & 0xFFFF);
    }
  }
}