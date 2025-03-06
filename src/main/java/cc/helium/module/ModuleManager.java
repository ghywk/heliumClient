package cc.helium.module;

import cc.helium.module.impl.movement.Sprint;
import cc.helium.module.impl.render.BetterItem;
import cc.helium.module.impl.render.ClickGUI;
import cc.helium.module.impl.render.HUDModule;
import cc.helium.util.Util;
import cc.helium.util.logging.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kev1nLeft
 */

public class ModuleManager implements Util {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        this.load();
    }

    public Module getModuleByNameStrict(String name){
        for (Module module : modules) {
            if (name.equals(module.getName())) {
                return module;
            }
        }
        return null;
    }

    public Module getModuleByName(String name){
        for (Module module : modules) {
            if (name.equalsIgnoreCase(module.getName())) {
                return module;
            }
        }
        return null;
    }

    public List<Module> getEnableModules(){
        return modules.stream().filter(Module::isEnable).collect(Collectors.toList());
    }

    public List<Module> getModulesInCategory(Category category) {
        return modules.stream().filter(module -> module.getCategory().equals(category)).collect(Collectors.toList());
    }

    public <T>T getModule(Class<T> clazz) {
        for (Module m : modules) {
            if (m.getClass() == clazz) {
                return (T) m;
            }
        }
        return (T) null;
    }

    public void onKey(int key){
        for (Module module : getModules()) {
            if (module.getKey() == key) {
                module.setEnable(!module.isEnable());
            }
        }
    }

    public void load(){
        // 1.0.0
        add(new Sprint());
        add(new ClickGUI());
        add(new HUDModule());
        add(new BetterItem());

        LogUtil.log_info("Module Loaded");
    }
    public List<Module> getModules() {
        return modules;
    }

    private void add(Module module) {
        this.modules.add(module);
    }
}
