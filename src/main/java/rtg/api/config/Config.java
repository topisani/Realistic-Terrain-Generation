package rtg.api.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import rtg.api.config.property.*;
import rtg.api.config.property.ConfigPropertyArray.ConfigPropertyArrayDouble;
import rtg.api.config.property.ConfigPropertyArray.ConfigPropertyArrayInteger;
import rtg.api.config.property.ConfigPropertyArray.ConfigPropertyArrayString;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public abstract class Config {

  public static final String NEW_LINE = "\n";

  static final String BLOCKSTATE_HELP =
      NEW_LINE + "Syntax : <ResourceLocation> [<IProperty name> = <value>, <IProperty name> = <value>, ...]" +
          NEW_LINE + "Example: minecraft:stone[variant=diorite], or minecraft:stained_glass_pane[color=pink,north=true,east=false,south=true,west=false]" +
          NEW_LINE + "For a list of property names and values, see: https://minecraft.gamepedia.com/Block_states";

  private final File configFile;
  protected List<ConfigProperty> properties = new ArrayList<>();
  private CommentedConfig config;

  protected Config(@Nonnull File configFile) {
    this.configFile = configFile;
  }

  protected Config() {
    this.configFile = null;
  }

  private void addProp(ConfigProperty property) {

    for (ConfigProperty prop : this.properties) {

      if (prop.getName().contentEquals(property.getName())) {
        removeProp(property.getName());
        break;
      }
    }

    this.properties.add(property);
  }

  private void removeProp(String name) {

    for (int i = 0; i < this.properties.size(); i++) {

      if (this.properties.get(i).getName().contentEquals(name)) {
        this.properties.remove(i);
        return;
      }
    }
  }

  public boolean hasProperty(ConfigProperty prop) {
    return this.properties.stream().anyMatch(property -> property.getCategory().contentEquals(prop.getCategory()) && property.getName().contentEquals(prop.getName()));
  }

  public final ConfigPropertyBoolean addProperty(ConfigPropertyBoolean property) {
    this.addProp(property);
    return property;
  }

  public final ConfigPropertyFloat addProperty(ConfigPropertyFloat property) {
    this.addProp(property);
    return property;
  }

  public final ConfigPropertyInteger addProperty(ConfigPropertyInteger property) {
    this.addProp(property);
    return property;
  }

  public final ConfigPropertyString addProperty(ConfigPropertyString property) {
    this.addProp(property);
    return property;
  }

  public final ConfigPropertyArrayInteger addProperty(ConfigPropertyArrayInteger property) {
    this.addProp(property);
    return property;
  }

  public final ConfigPropertyArrayDouble addProperty(ConfigPropertyArrayDouble property) {
    this.addProp(property);
    return property;
  }

  public final ConfigPropertyArrayString addProperty(ConfigPropertyArrayString property) {
    this.addProp(property);
    return property;
  }

  public void loadConfig() {
  }
}

