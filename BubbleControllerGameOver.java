import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

class BubbleControllerGameOver extends BubbleController
{
  private static final int DELAY_TIME = 400;
  private static final int SHOW_TIME = 2000;
  private static final int MAX_CNT = 5;
  private int m_time = 0;

  public boolean IsRepaint()
  {
    return true;
  }

  public void Final(BubbleGame paramBubbleGame)
  {
    super.SetActive(false);
    paramBubbleGame.ChangeDisplayToHighscore();
  }

  public void Draw(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    this.m_time += 1;
    if (this.m_time >= 5)
    {
      Final(paramBubbleGame);
      return;
    }
    if (BubbleController.m_offscreen == null)
    {
      Final(paramBubbleGame);
      return;
    }
    Graphics localGraphics = BubbleController.m_offscreen.getGraphics();
    paramBubbleGame.DrawBoard(localGraphics);
    paramBubbleGame.DrawScore(localGraphics);
    DrawBubbles(localGraphics, paramBubbleGame);
    int i = 13;
    int j = 46;
    localGraphics.setClip(i, j, 109, 37);
    localGraphics.drawImage(BubbleSmile.GetImage(), i, j - 33, 20);
    paramGraphics.setClip(0, 0, 128, 128);
    paramGraphics.drawImage(BubbleController.m_offscreen, 0, 0, 20);
  }

  public void KeyPressed(BubbleGame paramBubbleGame, int paramInt)
  {
    this.m_time += 1;
    if (this.m_time >= 5)
      Final(paramBubbleGame);
    else
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
        int k = paramBubbleGame.Pos2Pixel(b1, b2);
        int i = Bubble.rand.nextInt();
        i %= 3;
        int j = Bubble.rand.nextInt();
        j %= 3;
        localBubble.Draw(paramGraphics, localImage, (k & 0xFFFF) + i, (k >> 16 & 0xFFFF) + j);
      }
  }

  public int DelayTime()
  {
    return 400;
  }
}