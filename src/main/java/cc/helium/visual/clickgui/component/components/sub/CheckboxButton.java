package cc.helium.visual.clickgui.component.components.sub;

import cc.helium.value.impl.BoolValue;
import cc.helium.visual.clickgui.component.Component;
import cc.helium.visual.clickgui.component.components.Button;
import cc.helium.visual.font.FontManager;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CheckboxButton extends Component {

	private boolean hovered;
	private final BoolValue op;
	private final Button parent;
	private int offset;
	private int x;
	private int y;
	
	public CheckboxButton(BoolValue option, Button button, int offset) {
		this.op = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent() {
		int c1 = new Color(17, 17, 17, 140).getRGB(); // 0x88111111

        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, hovered ? 0x99000000 : 0x88000000);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, c1);
		GL11.glPushMatrix();
		GL11.glScalef(0.75f,0.75f, 0.75f);


		FontManager.arial18.drawString(this.hovered ? "§7" + this.op.getName() : this.op.getName(), (parent.parent.getX() + 3) * 1.3333333333f + 5, (parent.parent.getY() + offset + 2) * 1.3333333333f, new Color(255, 255, 255).getRGB());

        GL11.glPopMatrix();
		Gui.drawRect(parent.parent.getX() + parent.parent.getWidth() - 2, parent.parent.getY() + offset + 3, parent.parent.getX() + parent.parent.getWidth() - 8, parent.parent.getY() + offset + 9, 0x88999999);
		if(this.op.getValue()) {
			Gui.drawRect(parent.parent.getX() + parent.parent.getWidth() - 3, parent.parent.getY() + offset + 4, parent.parent.getX() + parent.parent.getWidth() - 7, parent.parent.getY() + offset + 8, 0x99000000);
		}
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
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
			this.op.setValue(!op.getValue());
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
