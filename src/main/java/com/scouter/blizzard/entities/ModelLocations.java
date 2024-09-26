package com.scouter.blizzard.entities;

import com.scouter.blizzard.Blizzard;
import net.minecraft.resources.ResourceLocation;

public class ModelLocations {

    public static ModelData MOTOR_FISH = new ModelData("motor_fish");
    public static ModelData PETAL_RAY = new ModelData("petal_ray");
    public static ModelData SPECTRAL_FIN = new ModelData("spectral_fin");
    public static ModelData BUBBLE_COPPERFISH = new ModelData("bubble_copperfish");
    public static ModelData BOREAL_EEL = new ModelData("boreal_eel");
    public static ModelData BOREAL_ANGEL = new ModelData("boreal_angel");
    public static ModelData SCARLET_MINNOW = new ModelData("scarlet_minnow");
    public static ModelData LAZULI_SCALE = new ModelData("lazuli_scale");
    public static ModelData GLITTERING_FISH = new ModelData("glittering_fish");
    public static ModelData EMERALDINE_MOLA = new ModelData("emeraldine_mola");
    public static ModelData AUREOFIN = new ModelData("aureofin");

    public static class ModelData {

        private ResourceLocation model;
        private ResourceLocation texture;
        private ResourceLocation animation;



        public ModelData(String name){
            this.model = new ResourceLocation(Blizzard.MODID, "geo/" + name + ".geo.json");
            this.texture = new ResourceLocation(Blizzard.MODID, "textures/entity/" + name + ".png");
            this.animation = new ResourceLocation(Blizzard.MODID, "animations/" + name + ".animation.json");
        }


        public ResourceLocation getAnimation() {
            return animation;
        }

        public ResourceLocation getModel() {
            return model;
        }

        public ResourceLocation getTexture() {
            return texture;
        }
    }
}
