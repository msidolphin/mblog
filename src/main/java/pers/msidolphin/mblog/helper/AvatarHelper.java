package pers.msidolphin.mblog.helper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据昵称生成用户头像
 * 修改自https://github.com/saysky/CreateNamePicture
 * Created by msidolphin on 2018/3/31.
 */
public class AvatarHelper {

	public static boolean isChinese(String test) {
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(test);
		if(matcher.find()) return true;
		return false;
	}

	/**
	 * 获得随机颜色
	 * @return
	 */
	private static Color getRandomColor() {
		String[] beautifulColors =
				new String[]{"232,221,203", "205,179,128", "3,101,100", "3,54,73", "3,22,52",
						"237,222,139", "251,178,23", "96,143,159", "1,77,103", "254,67,101", "252,157,154",
						"249,205,173", "200,200,169", "131,175,155", "229,187,129", "161,23,21", "34,8,7",
						"118,77,57", "17,63,61", "60,79,57", "95,92,51", "179,214,110", "248,147,29",
						"227,160,93", "178,190,126", "114,111,238", "56,13,49", "89,61,67", "250,218,141",
						"3,38,58", "179,168,150", "222,125,44", "20,68,106", "130,57,53", "137,190,178",
						"201,186,131", "222,211,140", "222,156,83", "23,44,60", "39,72,98", "153,80,84",
						"217,104,49", "230,179,61", "174,221,129", "107,194,53", "6,128,67", "38,157,128",
						"178,200,187", "69,137,148", "117,121,71", "114,83,52", "87,105,60", "82,75,46",
						"171,92,37", "100,107,48", "98,65,24", "54,37,17", "137,157,192", "250,227,113",
						"29,131,8", "220,87,18", "29,191,151", "35,235,185", "213,26,33", "160,191,124",
						"101,147,74", "64,116,52", "255,150,128", "255,94,72", "38,188,213", "167,220,224",
						"1,165,175", "179,214,110", "248,147,29", "230,155,3", "209,73,78", "62,188,202",
						"224,160,158", "161,47,47", "0,90,171", "107,194,53", "174,221,129", "6,128,67",
						"38,157,128", "201,138,131", "220,162,151", "137,157,192", "175,215,237", "92,167,186",
						"255,66,93", "147,224,255", "247,68,97", "185,227,217"};
		int len = beautifulColors.length;
		Random random = new Random();
		String[] color = beautifulColors[random.nextInt(len)].split(",");
		return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]),
				Integer.parseInt(color[2]));
	}


	/**
	 *
	 * @param nickname 用户昵称
	 * @param path		文件临时保存路径
	 * @param fileName	文件名称
	 * @return	{String} [文件保存路径]
	 * @throws IOException
	 */
	public static String generateAvatar(String nickname, String path, String fileName) throws IOException {
		if(nickname == null) {
			return null;
		}
		int width = 100;
		int height = 100;
		String text;
		String first = nickname.substring(0, 1);
		//如果不是中文则将首位英文字母转换为大写
		if(!isChinese(first))	text = nickname.substring(0, 1).toUpperCase();
		else text = first; //

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
		//开启抗锯齿
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setBackground(getRandomColor());
		g2d.clearRect(0, 0, width, height);
		g2d.setPaint(Color.WHITE);

		Font font = new Font("微软雅黑", Font.PLAIN, 60);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics(font);
		int textWidth = fm.stringWidth(text);
		int widthX = (width - textWidth) / 2;
		g2d.drawString(text, widthX, font.getSize() + height / 10);

		g2d.dispose();

//		BufferedImage rounded = makeRoundedCorner(bufferedImage, 99);
		String temp = path + File.separator + fileName + ".jpg";
		File file = new File(temp);
		ImageIO.write(bufferedImage, "png", file);
		return temp;
	}

	/**
	 * 图片做圆角处理
	 * @param image
	 * @param cornerRadius
	 * @return
	 */
	public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = output.createGraphics();
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return output;
	}
}
