package rtg.api.config;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.config.Property;
import rtg.api.util.BlockStringUtil;
import rtg.api.util.debug.RTGException;

/**
 * Wrapper for all different kinds of config properties
 *
 * @author topisani
 */
public abstract class ConfigProperty<T> {


    protected String id;
    protected String section;
    protected String comment;

    protected T defaultVal;
    protected T value;

    public ConfigProperty(String id, String section) {
        this.id = id;
        this.section = section;
    }

    public T get() {
        return this.value;
    }

    public T getDefault() {
        return defaultVal;
    }

    protected ConfigProperty<T> setDefault(T defaultVal) {
        this.defaultVal = defaultVal;
        return this;
    }

    public String getID() {
        return this.id;
    }

    public String getComment() {
        return comment;
    }

    public ConfigProperty<T> setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Needed for writing to config files
     *
     * @throws RTGException
     */
    public abstract Property toForgeProp() throws RTGException;

    /**
     * Needed for writing to config files
     *
     * @param prop The property to read
     * @return this
     * @throws RTGException of type CONFIG_SYNTAX if failed.
     */
    public abstract ConfigProperty readForgeProperty(Property prop) throws RTGException;

    protected ConfigProperty<T> set(T value) {
        this.value = value;
        return this;
    }

    public static class PropertyBool extends ConfigProperty<Boolean> {

        public PropertyBool(String id, String section) {
            super(id, section);
        }

        public ConfigProperty.PropertyBool setDefault(boolean defaultValue) {
            super.setDefault(defaultValue);
            return this;
        }
        public ConfigProperty.PropertyBool set(boolean value) {
            super.set(value);
            return this;
        }

        public ConfigProperty.PropertyBool setComment(String comment) {
            super.setComment(comment);
            return this;
        }

        /**
         * Needed for writing to config files
         *
         * @throws RTGException
         */
        public Property toForgeProp() throws RTGException {
            Property prop = new Property(id, String.valueOf(value), Property.Type.BOOLEAN).setDefaultValue(defaultVal);
            prop.set(value);
            return prop;
        }
        /**
         * Needed for writing to config files
         *
         * @param prop The property to read
         * @return this
         * @throws RTGException of type CONFIG_SYNTAX if failed.
         */
        public ConfigProperty readForgeProperty(Property prop) throws RTGException {
            try {
                this.set(prop.getBoolean());
                return this;
            } catch (Exception e) {
                throw new RTGException(RTGException.Type.CONFIG_SYNTAX,
                        "Tried to read property " + prop.getName() + " with value " + prop.getString() + " of type " + prop.getType().name() +
                                " into property " + id + " of type boolean",
                        "ConfigProperty.fromForgeProp()");
            }
        }
    }

    public static class PropertyInt extends ConfigProperty<Integer> {

        private int minValue;
        private int maxValue;

        public PropertyInt(String id, String section) {
            super(id, section);
        }

        public ConfigProperty.PropertyInt setDefault(int defaultValue) {
            super.setDefault(defaultValue);
            return this;
        }
        public ConfigProperty.PropertyInt set(int value) {
            super.set(value);
            return this;
        }

        public ConfigProperty.PropertyInt setRange(int minValue, int maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            return this;
        }

        public ConfigProperty.PropertyInt setComment(String comment) {
            super.setComment(comment);
            return this;
        }

        /**
         * Needed for writing to config files
         *
         * @throws RTGException
         */
        public Property toForgeProp() throws RTGException {
            Property prop = new Property(id, String.valueOf(value), Property.Type.INTEGER).setDefaultValue(defaultVal).setMinValue(minValue).setMaxValue(maxValue);
            prop.set(value);
            return prop;
        }

        /**
         * Needed for writing to config files
         *
         * @param prop The property to read
         * @return this
         * @throws RTGException of type CONFIG_SYNTAX if failed.
         */
        public ConfigProperty readForgeProperty(Property prop) throws RTGException {
            try {
                this.set(prop.getInt());
                return this;
            } catch (Exception e) {
                throw new RTGException(RTGException.Type.CONFIG_SYNTAX,
                        "Tried to read property " + prop.getName() + " with value " + prop.getString() + " of type " + prop.getType().name() +
                                " into property " + id + " of type integer",
                        "ConfigProperty.fromForgeProp()");
            }
        }
    }

    public static class PropertyString extends ConfigProperty<String> {

        public PropertyString(String id, String section) {
            super(id, section);
        }

        public ConfigProperty.PropertyString set(String value) {
            super.set(value);
            return this;
        }

        public ConfigProperty.PropertyString setDefault(String defaultValue) {
            super.setDefault(defaultValue);
            return this;
        }

        public ConfigProperty.PropertyString setComment(String comment) {
            super.setComment(comment);
            return this;
        }

        /**
         * Needed for writing to config files
         *
         * @throws RTGException
         */
        public Property toForgeProp() throws RTGException {
            Property prop = new Property(id, value, Property.Type.STRING).setDefaultValue(defaultVal);
            prop.set(value);
            return prop;
        }

        /**
         * Needed for writing to config files
         *
         * @param prop The property to read
         * @return this
         * @throws RTGException of type CONFIG_SYNTAX if failed.
         */
        public ConfigProperty readForgeProperty(Property prop) throws RTGException {
            try {
                this.set(prop.getString());
                return this;
            } catch (Exception e) {
                throw new RTGException(RTGException.Type.CONFIG_SYNTAX,
                        "Tried to read property " + prop.getName() + " with value " + prop.getString() + " of type " + prop.getType().name() +
                                " into property " + id + " of type string",
                        "ConfigProperty.fromForgeProp()");
            }
        }
    }

    public static class PropertyBlock extends ConfigProperty<IBlockState> {

        public PropertyBlock(String id, String section) {
            super(id, section);
        }

        public ConfigProperty.PropertyBlock set(IBlockState value) {
            super.set(value);
            return this;
        }

        public ConfigProperty.PropertyBlock setDefault(IBlockState defaultValue) {
            super.setDefault(defaultValue);
            return this;
        }

        public ConfigProperty.PropertyBlock setComment(String comment) {
            super.setComment(comment);
            return this;
        }
        /**
         * Needed for writing to config files
         *
         * @throws RTGException
         */
        public Property toForgeProp() throws RTGException {
            Property prop = new Property(id, BlockStringUtil.stateToString(value)   , Property.Type.STRING).setDefaultValue(BlockStringUtil.stateToString(defaultVal));
            prop.set(BlockStringUtil.stateToString(value));
            return prop;
        }

        /**
         * Needed for writing to config files
         *
         * @param prop The property to read
         * @return this
         * @throws RTGException of type CONFIG_SYNTAX if failed.
         */
        public ConfigProperty readForgeProperty(Property prop) throws RTGException {
            try {
                this.set(BlockStringUtil.stringToState(prop.getString()));
                return this;
            } catch (Exception e) {
                throw new RTGException(RTGException.Type.CONFIG_SYNTAX,
                        "Tried to read property " + prop.getName() + " with value " + prop.getString() + " of type " + prop.getType().name() +
                                " into property " + id + " of type block",
                        "ConfigProperty.fromForgeProp()");
            }
        }
    }
}
