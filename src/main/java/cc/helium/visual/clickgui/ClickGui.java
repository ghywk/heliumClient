package cc.helium.visual.clickgui;

import cc.helium.Client;
import cc.helium.module.Category;
import cc.helium.module.impl.render.ClickGUI;
import cc.helium.util.render.BlurUtil;
import cc.helium.visual.clickgui.component.Component;
import cc.helium.visual.clickgui.component.Frame;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGui extends GuiScreen {

	public static ArrayList<Frame> frames;
	public static final int color = 0x99cfdcff;

	public ClickGui() {
		frames = new ArrayList<>();
		int frameX = 5;
		for(Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		boolean blur = Client.getInstance().moduleManager.getModule(ClickGUI.class).blur.getValue();
		drawRect(0, 0, this.width, this.height, 0x66101010);
		if (blur) {
			BlurUtil.blurAll(0.1f);
		}
		for (Frame frame : frames) {
			frame.renderFrame();
			frame.updatePosition(mouseX, mouseY);
			for (Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		for(Frame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(Frame frame : frames) {
			if(frame.isOpen() && keyCode != 1) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}


		if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
	}

	@Override
	public void onGuiClosed() {
		if(this.mc.entityRenderer.getShaderGroup() != null) {
			this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
			mc.entityRenderer.theShaderGroup = null;
		}
		Client.getInstance().configManager.saveConfigs();

		super.onGuiClosed();
	}

	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(Frame frame : frames) {
			frame.setDrag(false);
		}
		for(Frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
