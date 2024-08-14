package az.ulvismayilov.offlineosmdemo.presentation

import android.graphics.Color
import androidx.compose.runtime.Stable
import androidx.compose.runtime.traceEventEnd
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.layer.overlay.Marker
import org.mapsforge.map.layer.overlay.Polygon
import org.mapsforge.map.layer.overlay.Polyline
import org.mapsforge.map.android.view.MapView


@Stable
class OsmMapViewProxy(
    //private val onOverlayClicked: (overlay: Overlay) -> Unit
) {

    private var mapView: MapView? = null
    private val pendingOperations = mutableListOf<(MapView) -> Unit>()

    fun setMapProxyView(mapView: MapView?) {
        println("setMapProxy: ${if (mapView != null) "not null" else "null"}")
        this.mapView = mapView
        if (mapView != null) {
            // Execute all pending operations
            pendingOperations.forEach { it(mapView) }
            pendingOperations.clear() // Clear after executing
        }
    }

    fun view(getOrDoMapViewOperation: (map: MapView) -> Unit) {
        if (mapView != null) {
            println("view: if")
            getOrDoMapViewOperation(mapView!!)
        } else {
            println("view: else")
            // Queue the operation if MapView is not ready yet
            pendingOperations.add(getOrDoMapViewOperation)
        }
    }



    /*fun addMarker(latitude: Double, longitude: Double, title: String) {
        view { mapView ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(latitude, longitude)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                this.title = title
            }
            marker.setOnMarkerClickListener { clickedMarker, _ ->
                //onOverlayClicked(clickedMarker)
                true
            }

            mapView.overlays.add(marker)
            mapView.invalidate() // Refresh the map view
        }
    }
*/

    /*fun addPolyline(points: List<GeoPoint>, color: Int = Color.BLUE) {
        view { mapView ->
            val polyline = Polyline().apply {
                this.setPoints(points)
                this.outlinePaint.color = color
            }
            mapView.overlays.add(polyline)
            mapView.invalidate() // Refresh the map view
        }
    }*/
   /* fun addPolygon(points: List<LatLong>, fillColor: Int = Color.argb(75, 0, 255, 0)) {
        view { mapView ->
            val polygon = Polygon().apply {
                this.points = points
                this.fillPaint.color = fillColor
            }
            mapView.overlays.add(polygon)
            mapView.invalidate() // Refresh the map view
        }
    }
*/
/*    fun clearOverlays() {
        view { mapView ->
            mapView.overlays.clear()
            mapView.invalidate() // Refresh the map view
        }
    }*/
}

