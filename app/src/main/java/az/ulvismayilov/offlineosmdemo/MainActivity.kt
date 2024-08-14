package az.ulvismayilov.offlineosmdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import az.ulvismayilov.offlineosmdemo.presentation.ComposeMapView
import az.ulvismayilov.offlineosmdemo.presentation.OsmMapViewProxy
import az.ulvismayilov.offlineosmdemo.ui.theme.OfflineOSMDemoTheme
import org.mapsforge.core.model.LatLong

//import org.osmdroid.util.GeoPoint

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            OfflineOSMDemoTheme {
                val mapProxy by remember {
                    mutableStateOf(OsmMapViewProxy())
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { /*TODO*/ },
                            navigationIcon = {
                                IconButton(onClick = {
                                    mapProxy.view { map ->
                                        /*val mapController = map.controller
                                        mapController.setZoom(8.5)
                                        val startPoint = GeoPoint(48.8583, 2.2944);
                                        mapController.setCenter(startPoint);*/
                                        map.setCenter(LatLong(40.4093, 49.8671))
                                        map.setZoomLevel(12)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->


                    ComposeMapView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        mapProxy = mapProxy
                    )


                }
            }
        }
    }
}
