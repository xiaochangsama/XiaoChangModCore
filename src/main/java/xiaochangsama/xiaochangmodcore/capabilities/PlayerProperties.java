package xiaochangsama.xiaochangmodcore.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiaochangsama.xiaochangmodcore.XiaoChangModCore;
import xiaochangsama.xiaochangmodcore.networks.ModMessages;
import xiaochangsama.xiaochangmodcore.networks.ModPacket;

import java.util.HashMap;

public class PlayerProperties {
    //使用hashmap来储存属性
    private HashMap<String, String> stringStringHashMap = new HashMap<>();
    private HashMap<String, Float> floatStringHashMap = new HashMap<>();
    private HashMap<String, Integer> integerStringHashMap = new HashMap<>();
    private HashMap<String, Boolean> booleanStringHashMap = new HashMap<>();

    //public static final String BLOOD = "气血";



    public PlayerProperties() {
        //floatStringHashMap.put(BLOOD, 200f);



    }

    //float数据4件套
    public float getFloatProperty(String key) {
        return floatStringHashMap.get(key);
    }

    public void setFloatProperty(String key, float value) {
        floatStringHashMap.put(key, value);
    }

    public void setFloatStringHashMap(HashMap<String, Float> hashMap) {
        floatStringHashMap = hashMap;
    }

    public HashMap<String, Float> getFloatStringHashMap() {
        return floatStringHashMap;
    }

    //String数据4件套
    public String getStringProperty(String key) {
        return stringStringHashMap.get(key);
    }

    public void setStringProperty(String key, String value) {
        stringStringHashMap.put(key, value);
    }

    public void setStringStringHashMap(HashMap<String, String> hashMap) {
        stringStringHashMap = hashMap;
    }

    public HashMap<String, String> getStringStringHashMap() {
        return stringStringHashMap;
    }

    //boolean数据4件套
    public boolean getBooleanProperty(String key) {
        return booleanStringHashMap.get(key);
    }

    public void setBooleanProperty(String key, boolean value) {
        booleanStringHashMap.put(key, value);
    }

    public void setBooleanStringHashMap(HashMap<String, Boolean> hashMap) {
        booleanStringHashMap = hashMap;
    }

    public HashMap<String, Boolean> getBooleanStringHashMap() {
        return booleanStringHashMap;
    }

    //int数据4件套
    public int getIntegerProperty(String key) {
        return integerStringHashMap.get(key);
    }

    public void setIntegerProperty(String key, int value) {
        integerStringHashMap.put(key, value);
    }

    public void setIntegerStringHashMap(HashMap<String, Integer> hashMap) {
        integerStringHashMap = hashMap;
    }

    public HashMap<String, Integer> getIntegerStringHashMap() {
        return integerStringHashMap;
    }

    //整体数据处理
    public void copy(PlayerProperties property) {
        setFloatStringHashMap(property.getFloatStringHashMap());
        setStringStringHashMap(property.getStringStringHashMap());
        setBooleanStringHashMap(property.getBooleanStringHashMap());
        setIntegerStringHashMap(property.getIntegerStringHashMap());
    }

    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        for (String key : floatStringHashMap.keySet()) {
            nbt.putFloat(key, floatStringHashMap.get(key));
        }
        for (String key : stringStringHashMap.keySet()) {
            nbt.putString(key, stringStringHashMap.get(key));
        }
        for (String key : booleanStringHashMap.keySet()) {
            nbt.putBoolean(key, booleanStringHashMap.get(key));
        }
        for (String key : integerStringHashMap.keySet()) {
            nbt.putInt(key, integerStringHashMap.get(key));
        }
        return nbt;
    }


    public void readNBT(CompoundTag nbt) {
        //this.blood = nbt.getInt("blood");
        for (String key : floatStringHashMap.keySet()) {
            this.floatStringHashMap.put(key, nbt.getFloat(key));
        }
        for (String key : stringStringHashMap.keySet()) {
            this.stringStringHashMap.put(key, nbt.getString(key));
        }
        for (String key : booleanStringHashMap.keySet()) {
            this.booleanStringHashMap.put(key, nbt.getBoolean(key));
        }
        for (String key : integerStringHashMap.keySet()) {
            this.integerStringHashMap.put(key, nbt.getInt(key));
        }


    }

    //在数据类新增hashMap之后，这个类完全不需要修改，数据会被自动处理
    //如果按照之前的逻辑，新增任何变量，这个类都需要加入好几个参数
    public static class PlayerPropertyProvider implements INBTSerializable<CompoundTag>, ICapabilityProvider {

        //创建提供调用的玩家能力实例
        public static Capability<PlayerProperties> PLAYER_PROPERTY = CapabilityManager.get(new CapabilityToken<PlayerProperties>() {
        });


        private PlayerProperties playerProperty = new PlayerProperties();

        //实现一个设置属性的工具类实例
        private final LazyOptional<PlayerProperties> playerPropertyHandler = LazyOptional.of(this::createPlayerProperty);

        //创建玩家属性
        private PlayerProperties createPlayerProperty() {
            if (this.playerProperty == null) {
                this.playerProperty = new PlayerProperties();
            }
            return this.playerProperty;
        }


        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == PLAYER_PROPERTY) {
                return this.playerPropertyHandler.cast();
            }
            return LazyOptional.empty();
        }

        //传入玩家实体,向客户端发包，支持每一tick发包
        public void syncPlayerProperty(ServerPlayer player) {
                ModMessages.sendToPlayer(new ModPacket(playerProperty), player);
        }

        @Override
        public CompoundTag serializeNBT() {
            return createPlayerProperty().writeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            createPlayerProperty().readNBT(nbt);
        }
    }

    @Mod.EventBusSubscriber(modid = XiaoChangModCore.MODID)
    public static class CapabilityEvent {

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerProperties.class);
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(new ResourceLocation(XiaoChangModCore.MODID, "xiao_chang_property"), new PlayerPropertyProvider());
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedInServer(PlayerEvent.PlayerLoggedInEvent event) {
            if (!event.getEntity().level.isClientSide()) {
                event.getEntity().getCapability(PlayerPropertyProvider.PLAYER_PROPERTY).ifPresent(oldState -> {
                    event.getEntity().getCapability(PlayerPropertyProvider.PLAYER_PROPERTY).ifPresent(newState -> {
                        newState.copy(oldState);
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onPlayerReSpawned(PlayerEvent.PlayerRespawnEvent event) {
            if (!event.getEntity().level.isClientSide()) {
                event.getEntity().getCapability(PlayerPropertyProvider.PLAYER_PROPERTY).ifPresent(playerProperty -> {

                });
            }

        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            //目前是死亡后会刷新数据
            if (!event.isWasDeath()) {
                event.getOriginal().getCapability(PlayerPropertyProvider.PLAYER_PROPERTY).ifPresent(oldState -> {
                    event.getOriginal().getCapability(PlayerPropertyProvider.PLAYER_PROPERTY).ifPresent(newState -> {
                        newState.copy(oldState);
                    });
                });
            }
        }
    }
}
