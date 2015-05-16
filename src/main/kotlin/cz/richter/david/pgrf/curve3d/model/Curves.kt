package cz.richter.david.pgrf.curve3d.model

import transforms3D.Kubika
import transforms3D.Point3D
import java.awt.Color
import java.util.ArrayList
import kotlin.properties.Delegates

/**
 * @author D.Richter
 * @since 16.5.2015
 */
public trait Curve {
    public fun getLines(): List<Triple<Point3D, Point3D, Color>>
}

abstract class AbstractCurve(val cubics: Kubika) : Curve {
    protected val immutableListOfLines: List<Triple<Point3D, Point3D, Color>> by Delegates.lazy {
        return@lazy  computeLines()
    }
    abstract protected fun computeLines(): List<Triple<Point3D, Point3D, Color>>

    override public fun getLines(): List<Triple<Point3D, Point3D, Color>> {
        return immutableListOfLines
    }
}


public class BezierCurve(val begin: Point3D = Point3D(),
                         val vectorBegin: Point3D = Point3D(),
                         val end: Point3D = Point3D(),
                         val vectorEnd: Point3D = Point3D(),
                         val color: Color = Color.WHITE,
                         val numberOfLines: Int = 20)
                : AbstractCurve(Cubics.bezier) {

    override protected final fun computeLines(): List<Triple<Point3D, Point3D, Color>> {
        val list: MutableList<Triple<Point3D, Point3D, Color>> = ArrayList()
        cubics.init(begin, vectorBegin, vectorEnd, end)

        var puv = begin
        for (i in 1..numberOfLines) {
            val t = i.toFloat() / numberOfLines
            val nas = cubics.compute(t.toDouble())
            list.add(Triple(puv, nas, color))
            puv = nas
        }

        return ArrayList(list)
    }
}

public class FergusonCurve(val begin: Point3D = Point3D(),
                           val secondPoint: Point3D = Point3D(),
                           val thirdPoint: Point3D = Point3D(),
                           val end: Point3D = Point3D(),
                           val color: Color = Color.WHITE,
                           val numberOfLines: Int = 20)
                : AbstractCurve(Cubics.ferguson) {

    override protected fun computeLines(): List<Triple<Point3D, Point3D, Color>> {
        throw UnsupportedOperationException("Not implemented yet")
    }
}


public class CoonsCurve(val begin: Point3D = Point3D(),
                        val secondPoint: Point3D = Point3D(),
                        val thirdPoint: Point3D = Point3D(),
                        val end: Point3D = Point3D(),
                        val color: Color = Color.WHITE,
                        val numberOfLines: Int = 20)
                : AbstractCurve(Cubics.coons) {

    override protected fun computeLines(): List<Triple<Point3D, Point3D, Color>> {
        throw UnsupportedOperationException("Not implemented yet")
    }
}

object Cubics {
    val bezier = Kubika(0)
    val ferguson = Kubika(1)
    val coons = Kubika(2)
}

