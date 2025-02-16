package cc.helium.visual.clickgui.component.components.sub;

import cc.helium.value.impl.ModeValue;
import cc.helium.visual.clickgui.component.Component;
import cc.helium.visual.clickgui.component.components.Button;
import cc.helium.visual.font.FontHelper;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class ModeButton extends Component {

	private boolean hovered;
	private final Button parent;
	private final ModeValue set;
	private int offset;
	private int x;
	private int y;
	
	public ModeButton(ModeValue set, Button button, int offset) {
		this.set = set;
		this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		int c1 = new Color(17, 17, 17, 140).getRGB(); // 0x88111111

		Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, hovered ? 0x99000000 : 0x88000000);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, c1);
		GL11.glPushMatrix();
		GL11.glScalef(0.75f,0.75f, 0.75f);

        FontHelper.verdana32.drawString(this.hovered ? "§7" + set.getName() + " " : set.getName() + " ", (parent.parent.getX() + 7) * 1.33333333333f, (parent.parent.getY() + offset + 2) * 1.33333333333f, Color.white);
		FontHelper.verdana32.drawString(set.getValue(), (parent.parent.getX() + 86) * 1.33333333333f - FontHelper.verdana32.getStringWidth(set.getValue()), (parent.parent.getY() + offset + 2) * 1.33333333333f, Color.white);

        GL11.glPopMatrix();
	}


	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			set.next();
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
