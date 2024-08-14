package az.ulvismayilov.offlineosmdemo.presentation

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import az.ulvismayilov.offlineosmdemo.R
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.Layer
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@Composable
fun ComposeMapView(
    modifier: Modifier = Modifier,
    mapProxy: OsmMapViewProxy? = null,
    overlays: List<Layer>? = null,
    /* tileSource: ITileSource = TileSourceFactory.DEFAULT_TILE_SOURCE,

     onMapClick: (GeoPoint) -> Unit = {},
     onMapLongClick: (GeoPoint) -> Unit = {},*/
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current

    val mapView = remember {
        MapView(context)
    }

    fun copyMapFileToInternalStorage(fileName: String, context: Context): String? {
        val mapDirPath = "${context.filesDir}"


        val file = File(mapDirPath, fileName)

        if (!file.exists()) {
            try {
                context.assets.open(fileName).use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                       inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }

        return file.absolutePath
    }


    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    /* org.osmdroid.config.Configuration
                    .getInstance()
                    .load(
                        context,
                        context.getSharedPreferences("osm", Context.MODE_PRIVATE)
                    )*/
                    AndroidGraphicFactory.createInstance(context.applicationContext)
                }

                /*Lifecycle.Event.ON_RESUME -> //mapView.onResume()
                    Lifecycle.Event.ON_PAUSE-> mapView.onPause()*/
                Lifecycle.Event.ON_DESTROY -> {
                    //mapView.layerManager.layers.clear()
                    //mapView.destroyAll()
                    //AndroidGraphicFactory.clearResourceMemoryCache()
                }
                else -> Unit
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            try {
                /*mapView.onDetach()*/
            } finally {
                lifecycle.lifecycle.removeObserver(observer)
            }

        }
    }


    Box(modifier = modifier.clipToBounds()) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = "OsmMapView" },
            factory = {
                mapView.also { map ->
                    map.mapScaleBar.isVisible = true
                    map.setBuiltInZoomControls(true)

                    map.setLayerType(GLSurfaceView.LAYER_TYPE_HARDWARE, null)
                    println("layerRenderType: ${map.layerType}")


                    val worldFilePath = copyMapFileToInternalStorage("world.map", context)
                    worldFilePath?.let {
                        val worldMapFile = MapFile(it)
                        val worldTileCache = AndroidUtil.createTileCache(
                            context, "worldMapCache", mapView.model.displayModel.tileSize, 1f,
                            mapView.model.frameBufferModel.overdrawFactor
                        )

                        val worldMapRenderedLayer = TileRendererLayer(
                            worldTileCache,
                            worldMapFile,
                            mapView.model.mapViewPosition,
                            false,
                            true,
                            true,
                            AndroidGraphicFactory.INSTANCE
                        )

                        worldMapRenderedLayer.setXmlRenderTheme(
                            InternalRenderTheme.OSMARENDER
                        )

                        map.layerManager.layers.add(worldMapRenderedLayer)
                    }

                    val azerbaijanFilePath = copyMapFileToInternalStorage("azerbaijan.map", context)
                    azerbaijanFilePath?.let {
                        val azerbaijanMapFile = MapFile(it)
                        val azerbaijanTileCache = AndroidUtil.createTileCache(
                            context, "azerbaijanMapCache", mapView.model.displayModel.tileSize, 1f,
                            mapView.model.frameBufferModel.overdrawFactor
                        )

                        val azerbaijanMapRenderedLayer = TileRendererLayer(
                            azerbaijanTileCache,
                            azerbaijanMapFile,
                            mapView.model.mapViewPosition,
                            true,
                            true,
                            true,
                            AndroidGraphicFactory.INSTANCE
                        )

                        azerbaijanMapRenderedLayer.setXmlRenderTheme(
                            InternalRenderTheme.OSMARENDER
                        )

                        // Optionally adjust the layer visibility based on zoom level or position
                        //azerbaijanMapRenderedLayer.isVisible = mapView.model.mapViewPosition.zoomLevel >= 10


                        map.layerManager.layers.add(azerbaijanMapRenderedLayer)
                    }



                    map.layerManager.layers.apply {
                        if (overlays != null && this != overlays) {
                            this.clear()
                            this.addAll(overlays)
                        }
                    }
                }


            },
            update = {

            }

        )
    }


    DisposableEffect(mapView) {
        mapProxy?.setMapProxyView(mapView)
        onDispose {
            mapProxy?.setMapProxyView(null)
        }
    }


}