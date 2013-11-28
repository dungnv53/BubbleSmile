import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

class BubbleControllerMove extends BubbleController
{
  private static final int MAX_STEP = 0;
  private static final byte[] m_step_delta_x = { 5, 7 };
  private static final byte[] m_step_delta_y = { 4, 8 };
  private byte m_step;
  private boolean m_immediate_mode = false;

  public void Init(BubbleGame paramBubbleGame, Image paramImage)
  {
    this.m_step = 0;
    BubbleController.m_offscreen = paramImage;
  }

  public void Final(BubbleGame paramBubbleGame)
  {
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    localBubblePool.MoveBubble();
    super.SetActive(false);
  }

  public void KeyPressed(BubbleGame paramBubbleGame, int paramInt)
  {
  }

  public void Draw(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    paramGraphics.drawImage(BubbleController.m_offscreen, 0, 0, 20);
    paramBubbleGame.DrawScore(paramGraphics);
    paramBubbleGame.DrawConstraint(paramGraphics);
    DrawBubbles(paramGraphics, paramBubbleGame, false);
    this.m_step = (byte)(this.m_step + 1);
    if (this.m_step < 0)
      return;
    Final(paramBubbleGame);
  }

  public void DrawBubbles(Graphics paramGraphics, BubbleGame paramBubbleGame, boolean paramBoolean)
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
        i = ApplyDirectionWithStep(localBubble, i);
        localBubble.NextMovingStatus();
        localBubble.Draw(paramGraphics, localImage, i & 0xFFFF, i >> 16 & 0xFFFF);
      }
  }

  public static int ApplyDirectionWithStep(Bubble paramBubble, int paramInt)
  {
    int i = paramInt & 0xFFFF;
    int j = paramInt >> 16 & 0xFFFF;
    switch (paramBubble.GetDirection())
    {
    case 1:
      i += m_step_delta_x[0];
      j -= m_step_delta_y[0];
      break;
    case 2:
      i += m_step_delta_x[0];
      j += m_step_delta_y[0];
      break;
    case 3:
      i -= m_step_delta_x[0];
      break;
    case -1:
      i -= m_step_delta_x[0];
      j += m_step_delta_y[0];
      break;
    case -2:
      i -= m_step_delta_x[0];
      j -= m_step_delta_y[0];
      break;
    case -3:
      i += m_step_delta_x[0];
    case 0:
    }
    return i | j << 16;
  }

  public int DelayTime()
  {
    return 0;
  }

  public void SetImmediateMode(boolean paramBoolean)
  {
    this.m_immediate_mode = paramBoolean;
  }
}