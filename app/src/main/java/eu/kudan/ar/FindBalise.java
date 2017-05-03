package eu.kudan.ar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.jme3.math.Vector3f;

import java.io.File;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARArbiTrackListener;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARTexture2D;
import eu.kudan.kudan.ARTextureMaterial;

public class FindBalise extends ARActivity {
    boolean detected = false;
    File pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_balise);
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("sVmoznmKZ+4nFEHD6HoslwpC26PNuBZGHrikUwyon2BKSvza1yu2CqbSrae+pHPr1NHjhsf5pHQOZn8IEqXlqXFodGsrOJhxJANbMOdvnRLUi9/QWGqyRL9FViDmyohw6e5R7U4Ex8H7d7spLLvhfp5HFv56DgLr8c8sC2ipDtv9g1IjOTaY7UGxata3eulG2A/UkIdRv2NcotZXqan01xQUWFAislEwlGguParEYiwu11T4mqtU3dQBbfxpvxbczjdYz493YG3rAO2RHgT+5M5TJShJsz2irkNo71JD2Fzqf4AR2b4+7t1c55zKjegXzGS6Xa/rpNn9yiXUn7rUYIHNvN3cEQa9HsZiVxAV4vJgxFS+T/AxfWqKrEg1uj6xF5MsodZ2EkZ8mqliYIsxZqnFz+Re2HeWG8wvrEob0ZwRIO0TxppAemZc3HChTAPLcNt5gzeBk0oRP4wnrFAFFBDi8XjDocwTSVw++hWZb1qNHzt6bKLsMDRT057UVuuZB6M8f7EOQD79Oah0Vrx/3DUK6e9BEV8oGFNHtk1wyYEkg0i6RLhVSokGx//Qj36A4gCz3h1OjtfB0OuukbNq7xI1L/FcNQLmGYNGZwszARjGr9ESw1gVAkbQMxaV27uo/KoIq4+nR7RL8iT7t7NAaXCFIi24RR+7WGjTvKqWYjA=");

        if(getIntent().getExtras() != null)
        {
            pic = (File)getIntent().getExtras().get("picture");
        }
    }

    @Override
    public void onBackPressed(){
        System.out.println("back pressed");
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


    public ARImageTrackable createMarker (String picture)
    {
        ARImageTrackable trackable= new ARImageTrackable("randomName");
        trackable.loadFromAsset(picture);
        return trackable;
    }

    public ARImageTrackable createMarker (File file)
    {
        ARImageTrackable trackable= new ARImageTrackable(file.getName());
        trackable.loadFromPath(file.getPath());
        return trackable;
    }

    public ARModelNode create3DModel(String model)
    {
        ARModelImporter modelImporter = new ARModelImporter();
        modelImporter.loadFromAsset(model);
        ARModelNode modelNode = (ARModelNode)modelImporter.getNode();

        // Apply model texture file to model texture material and add ambient lighting
        ARLightMaterial material = new ARLightMaterial();
        material.setAmbient(0.8f, 0.8f, 0.8f);

        // Apply texture material to models mesh nodes
        for(ARMeshNode meshNode : modelImporter.getMeshNodes())
        {
            meshNode.setMaterial(material);
        }

        return modelNode;
    }

    public ARModelNode create3DModel(String model,String texture)
    {
        ARModelImporter modelImporter = new ARModelImporter();
        modelImporter.loadFromAsset(model);
        ARModelNode modelNode = (ARModelNode)modelImporter.getNode();

        // Load model texture
        ARTexture2D texture2D = new ARTexture2D();
        texture2D.loadFromAsset(texture);

        // Apply model texture file to model texture material and add ambient lighting
        ARLightMaterial material = new ARLightMaterial();
        material.setTexture(texture2D);
        material.setAmbient(0.8f, 0.8f, 0.8f);

        // Apply texture material to models mesh nodes
        for(ARMeshNode meshNode : modelImporter.getMeshNodes())
        {
            meshNode.setMaterial(material);
        }

        return modelNode;
    }

    public ARImageNode get2DModel(String name)
    {
        return new ARImageNode(name);
    }

    public ARImageTracker getTracker(ARNode model,ARImageTrackable marker)
    {
        ARImageTracker trackableManager =ARImageTracker.getInstance();
        //ajout de l'image au manager
        trackableManager.addTrackable(marker);

        marker.getWorld().addChild(model);
        return trackableManager;

    }

    public void scaleImage (ARImageNode imageNode, ARImageTrackable trackable) {
        //Set scale ================
        ARTextureMaterial textureMaterial = (ARTextureMaterial) imageNode.getMaterial();
        float scale = trackable.getWidth() / textureMaterial.getTexture().getWidth();
        // Apply it to your ARNode
        imageNode.scaleByUniform(scale);

    }

    public void scale3D (ARModelNode node, float scale) {
        //Set scale ================
        node.scaleByUniform(scale);
    }
    //==========================




    public void setArbitrackModel(ARNode modelNode){
        // Get ArbiTrack instance
        ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();

        // Get model nodes position relative to camera
        Vector3f fullPos = arArbiTrack.getTargetNode().getFullTransform().mult(Vector3f.ZERO);

        // Get models position relative to ArbiTracks world.
        Vector3f posInArbiTrack = arArbiTrack.getWorld().getFullTransform().invert().mult(fullPos);

        /*
        // Get models orientation relative to ArbiTracks world.
        Quaternion orInArbiTrack = arArbiTrack.getWorld().getOrientation().mult((modelNode.getOrientation()));
        */
        // Add the model node as a child of ArbiTrack
        arArbiTrack.getWorld().addChild(modelNode);

        // Change model nodes position to be relative to the marker nodes world
        modelNode.setPosition(posInArbiTrack);

        /*
        // Change model nodes orientation to be relative to the marker nodes world
        modelNode.setOrientation(orInArbiTrack);
        */
    }
    @Override
    public void setup() {
        super.setup();

        //création marker====================
        final ARImageTrackable trackable;
        if(pic != null)
            trackable = createMarker(pic);
        else
            trackable = createMarker ("virsolvy.jpg");
        //Modèle 3D==============================
        final ARModelNode modelNode =create3DModel("ben.jet", "bigBenTexture.png");
        scale3D(modelNode, (float) 0.4);

        //création tracking manager==========
        ARImageTracker trackableManager =getTracker(modelNode,trackable);//3D

        ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();

        // Add Activity to ARArbiTrack listeners
        trackable.addListener(new ARImageTrackableListener() {
            @Override
            public void didDetect(ARImageTrackable arImageTrackable) {
                ARArbiTrack.getInstance().start();
                detected = true;
                CharSequence text = "Balise détectée ! Touchez l'écran";
                int duration = Toast.LENGTH_SHORT;
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void didTrack(ARImageTrackable arImageTrackable) {}
            @Override
            public void didLose(ARImageTrackable arImageTrackable) {}
        });

        arArbiTrack.initialise();
        arArbiTrack.addListener(new ARArbiTrackListener() {
            @Override
            public void arbiTrackStarted() {
                setArbitrackModel(modelNode);
            }
        });

        ARNode targetNode = new ARNode();
        targetNode.setName("targetNode");

        // Add target node to image trackable world
        trackable.getWorld().addChild(targetNode);

        // Set blank node as target node for ArbiTrack
        arArbiTrack.setTargetNode(targetNode);


        this.getARView().getRootView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("detected", detected);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                return false;
            }
        });
    }

}

//2D EXAMPLE
//Modèle 2D==============================
//final ARImageNode imageNode = get2DModel("eyebrow.png");
//imageNode.rotateByDegrees(90, 0, 1, 0);
//========================================
//ARImageTracker trackableManager =getTracker(imageNode,trackable);//2D
//setArbitrackModel(imageNode);
