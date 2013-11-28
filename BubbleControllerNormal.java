import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class BubbleControllerNormal extends BubbleController
{
  private byte[] m_bonus_flag = null;
  private int[] m_bonus_bondary = null;

  public boolean IsRepaint()
  {
    return true;
  }

  public void Init(BubbleGame paramBubbleGame)
  {
    Graphics localGraphics = BubbleController.m_offscreen.getGraphics();
    DrawOffscreen(localGraphics, paramBubbleGame);
    this.m_bonus_flag = new byte[5];
    for (int i = 0; i < this.m_bonus_flag.length; ++i)
      this.m_bonus_flag[i] = 0;
    this.m_bonus_bondary = new int[6];
    this.m_bonus_bondary[0] = 500;
    this.m_bonus_bondary[1] = 2000;
    this.m_bonus_bondary[2] = 4000;
    this.m_bonus_bondary[3] = 6000;
    this.m_bonus_bondary[4] = 8000;
    this.m_bonus_bondary[5] = 16000;
  }

  public void AdjustBonusFlagByScore(int paramInt)
  {
    for (int i = 0; i < 5; ++i)
    {
      if (paramInt <= this.m_bonus_bondary[i])
        continue;
      this.m_bonus_flag[i] = 1;
    }
  }

  public void Draw(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    BubbleGameMode localBubbleGameMode = paramBubbleGame.GetBubbleGameMode();
    if (!localBubbleGameMode.IsValid())
    {
      BubbleControllerGameOver localBubbleControllerGameOver = new BubbleControllerGameOver();
      super.SetActive(false);
      localBubbleControllerGameOver.SetActive(true);
      return;
    }
    int i = 0;
    ScorePool localScorePool = paramBubbleGame.GetScorePool();
    int j = localScorePool.GetTotalScore();
    for (int k = 0; k < 5; ++k)
    {
      if ((this.m_bonus_flag[k] != 0) || (j < this.m_bonus_bondary[k]) || (j >= this.m_bonus_bondary[(k + 1)]))
        continue;
      this.m_bonus_flag[k] = 1;
      localObject = paramBubbleGame.GetChecker();
      ((Checker)localObject).SetBonusMode();
      BubbleSmile.PlaySound(6);
      i = 1;
      break;
    }
    if (localBubbleGameMode.IsTimedMode())
      localBubbleGameMode.MoveTimer();
    paramGraphics.drawImage(BubbleController.m_offscreen, 0, 0, 20);
    DrawBubbles(paramGraphics, paramBubbleGame);
    paramBubbleGame.DrawScore(paramGraphics);
    paramBubbleGame.DrawConstraint(paramGraphics);
    paramBubbleGame.DrawCursor(paramGraphics);
    paramBubbleGame.DrawCombos(paramGraphics);
    if (i != 1)
      return;
    localScorePool.Clear();
    Object localObject = new BubbleControllerDestroy();
    BubbleControllerDownAndFill.FirstTime();
    ((BubbleControllerDestroy)localObject).Init(paramBubbleGame, BubbleController.m_offscreen);
    ((BubbleControllerDestroy)localObject).SetRepaintFlag();
    ((BubbleController)localObject).SetActive(true);
    paramBubbleGame.repaint();
  }

  void DrawOffscreen(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
    paramBubbleGame.DrawBoard(paramGraphics);
    BubblePool localBubblePool = paramBubbleGame.GetBubblePool();
    Image localImage = paramBubbleGame.GetBubbleImages();
    for (byte b2 = 0; b2 < 9; b2 = (byte)(b2 + 1))
      for (byte b1 = 0; b1 < 7; b1 = (byte)(b1 + 1))
      {
        Bubble localBubble = localBubblePool.GetBubble(b1, b2);
        if (localBubble == null)
          continue;
        int i = paramBubbleGame.Pos2Pixel(b1, b2);
        localBubble.Draw(paramGraphics, localImage, i & 0xFFFF, i >> 16 & 0xFFFF);
      }
    BubbleGameMode localBubbleGameMode = paramBubbleGame.GetBubbleGameMode();
    if (!localBubbleGameMode.IsSkilledMode())
      return;
    paramGraphics.setClip(76, 1, 21, 8);
    paramGraphics.drawImage(BubbleSmile.GetImage(), 76, -24, 20);
  }

  public void KeyPressed(BubbleGame paramBubbleGame, int paramInt)
  {
    paramBubbleGame.GetScorePool().Clear();
    Cursor localCursor = paramBubbleGame.GetCursor();
    BubbleGameMode localBubbleGameMode = paramBubbleGame.GetBubbleGameMode();
    switch (paramInt)
    {
    case -3:
    case 52:
      localCursor.Move(0);
      break;
    case -4:
    case 54:
      localCursor.Move(1);
      break;
    case -1:
    case 50:
      localCursor.Move(2);
      break;
    case -2:
    case 56:
      localCursor.Move(3);
      break;
    case 49:
    case 51:
      BubbleController localBubbleController = BubbleController.CurrentController();
      if (localBubbleController == null)
        return;
      if (!localBubbleController.getClass().getName().equals("BubbleControllerNormal"))
        return;
      if (localBubbleGameMode.IsSkilledMode())
        localBubbleGameMode.DecreaseRotation();
      BubbleControllerRotator localBubbleControllerRotator = new BubbleControllerRotator();
      localBubbleControllerRotator.Init(paramBubbleGame, paramInt == 51, BubbleController.m_offscreen);
      localBubbleControllerRotator.SetActive(true);
      paramBubbleGame.repaint();
      break;
    default:
      return;
    }
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
        localBubble.RandomizeNormalStatus();
        if (!localBubble.IsGrin())
          continue;
        int i = paramBubbleGame.Pos2Pixel(b1, b2);
        localBubble.Draw(paramGraphics, localImage, i & 0xFFFF, i >> 16 & 0xFFFF);
      }
  }
}