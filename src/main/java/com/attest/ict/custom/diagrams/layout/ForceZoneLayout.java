package com.attest.ict.custom.diagrams.layout;

import static com.powsybl.sld.model.Coord.Dimension.X;
import static com.powsybl.sld.model.Coord.Dimension.Y;

import com.powsybl.sld.force.layout.ForceLayout;
import com.powsybl.sld.force.layout.Vector;
import com.powsybl.sld.layout.*;
import com.powsybl.sld.model.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.graph.Pseudograph;

public class ForceZoneLayout extends AbstractSubstationLayout implements ZoneLayout {

    private ZoneGraph zoneGraph;

    private SubstationLayoutFactory sLayoutFactory;

    private ForceSubstationLayoutFactory.CompactionType compactionType;

    public static class ForceInfoCalcPoints extends InfoCalcPoints {

        private String sId1;
        private String sId2;
        private Map<String, Map<BusCell.Direction, Integer>> nbSnakeLinesTopBottom;
        private Map<String, Integer> nbSnakeLinesBetween;

        public String getsId1() {
            return sId1;
        }

        public void setsId1(String sId1) {
            this.sId1 = sId1;
        }

        public String getsId2() {
            return sId2;
        }

        public void setsId2(String sId2) {
            this.sId2 = sId2;
        }

        public Map<String, Map<BusCell.Direction, Integer>> getNbSnakeLinesTopBottom() {
            return nbSnakeLinesTopBottom;
        }

        public void setNbSnakeLinesTopBottom(Map<String, Map<BusCell.Direction, Integer>> nbSnakeLinesTopBottom) {
            this.nbSnakeLinesTopBottom = nbSnakeLinesTopBottom;
        }

        public Map<String, Integer> getNbSnakeLinesBetween() {
            return nbSnakeLinesBetween;
        }

        public void setNbSnakeLinesBetween(Map<String, Integer> nbSnakeLinesBetween) {
            this.nbSnakeLinesBetween = nbSnakeLinesBetween;
        }
    }

    public ForceZoneLayout(
        ZoneGraph zoneGraph,
        SubstationLayoutFactory sLayoutFactory,
        VoltageLevelLayoutFactory voltageLevelLayoutFactory,
        ForceSubstationLayoutFactory.CompactionType compactionType
    ) {
        super(null, voltageLevelLayoutFactory);
        this.zoneGraph = zoneGraph;
        this.sLayoutFactory = sLayoutFactory;
        this.compactionType = compactionType;
    }

    private Graph<SubstationGraph, Object> toJgrapht(ZoneGraph zoneGraph) {
        Graph<SubstationGraph, Object> graph = new Pseudograph<>(Object.class);

        for (SubstationGraph substationGraph : zoneGraph.getNodes()) {
            graph.addVertex(substationGraph);
        }

        for (LineEdge edge : zoneGraph.getLineEdges()) {
            Optional<SubstationGraph> substationGraph1 = zoneGraph
                .getNodes()
                .stream()
                .filter(x -> x.getVLGraph(edge.getNode1().getGraph().getId()) == edge.getNode1().getGraph())
                .findFirst();
            Optional<SubstationGraph> substationGraph2 = zoneGraph
                .getNodes()
                .stream()
                .filter(x -> x.getVLGraph(edge.getNode2().getGraph().getId()) == edge.getNode2().getGraph())
                .findFirst();
            graph.addEdge(substationGraph1.get(), substationGraph2.get());
        }

        return graph;
    }

    @Override
    public void run(LayoutParameters layoutParameters) {
        // parameters for compact layout
        layoutParameters.setAdaptCellHeightToContent(false);
        layoutParameters.setHorizontalSubstationPadding(100.0);
        layoutParameters.setVerticalSubstationPadding(1.0);
        layoutParameters.setInitialYBus(50.0);
        layoutParameters.setVerticalSpaceBus(10.0);
        layoutParameters.setHorizontalBusPadding(10.0);
        layoutParameters.setCellWidth(20.0);
        layoutParameters.setExternCellHeight(60.0);
        layoutParameters.setInternCellHeight(20.0);
        layoutParameters.setStackHeight(1.0);

        // using new ForceLayout
        Graph<SubstationGraph, Object> graph = toJgrapht(zoneGraph);

        ForceLayout<SubstationGraph, Object> forceLayout = new ForceLayout<>(graph);
        forceLayout.execute();

        Map<SubstationGraph, Coord> coordsSubstations = new HashMap<>();

        // Creating and applying the substations layout with these coordinates
        for (SubstationGraph substationGraph : zoneGraph.getNodes()) {
            Vector position = forceLayout.getStablePosition(substationGraph);
            coordsSubstations.put(substationGraph, new Coord(position.getX(), position.getY()));
        }

        Map<SubstationGraph, SubstationLayout> graphsLayouts = new HashMap<>();
        coordsSubstations
            .entrySet()
            .forEach(e -> {
                SubstationLayout sLayout = sLayoutFactory.create(e.getKey(), vLayoutFactory);
                graphsLayouts.put(e.getKey(), sLayout);
                sLayout.run(layoutParameters);
            });

        // changingCellsOrientation();

        // List of substations sorted by ascending x value
        List<SubstationGraph> graphsX = coordsSubstations
            .entrySet()
            .stream()
            .sorted(Comparator.comparingDouble(e -> e.getValue().get(X)))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // List of substations sorted by ascending y value
        List<SubstationGraph> graphsY = coordsSubstations
            .entrySet()
            .stream()
            .sorted(Comparator.comparingDouble(e -> e.getValue().get(Y)))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // Map with substationId and Coord(X,Y)
        Map<String, Coord> newCoords = new LinkedHashMap<>();

        // Narrowing / Spreading the substations in the horizontal direction
        // (if no compaction, one substation only in a column
        //  if horizontal compaction, a substation is positioned horizontally at the middle of the preceding one)
        double graphX = getHorizontalSubstationPadding(layoutParameters);
        for (SubstationGraph s : graphsX) {
            newCoords.put(s.getSubstationId(), new Coord(graphX, 0.0));
            for (VoltageLevelGraph g : s.getNodes()) {
                graphX +=
                    layoutParameters.getInitialXBus() +
                    (g.getMaxH() + 2) *
                    layoutParameters.getCellWidth() +
                    getHorizontalSubstationPadding(layoutParameters) -
                    (compactionType == ForceSubstationLayoutFactory.CompactionType.NONE ? 200.0 : 0.0);
                if (compactionType == ForceSubstationLayoutFactory.CompactionType.HORIZONTAL) {
                    graphX /= 2;
                }
            }
        }

        // Narrowing / Spreading the substations in the vertical direction
        // (if no compaction, one substation only in a line
        //  if vertical compaction, a substation is positioned vertically at the middle of the preceding one)
        double graphY = getVerticalSubstationPadding(layoutParameters);
        for (SubstationGraph s : graphsY) {
            newCoords.get(s.getSubstationId()).set(Y, graphY);
            for (VoltageLevelGraph g : s.getNodes()) {
                graphY +=
                    (compactionType == ForceSubstationLayoutFactory.CompactionType.NONE ? 50.0 : layoutParameters.getInitialYBus()) +
                    (compactionType == ForceSubstationLayoutFactory.CompactionType.NONE ? 10.0 : layoutParameters.getStackHeight()) +
                    (compactionType == ForceSubstationLayoutFactory.CompactionType.NONE ? 10.0 : layoutParameters.getExternCellHeight()) +
                    layoutParameters.getVerticalSpaceBus() *
                    (g.getMaxV() + 2) +
                    getVerticalSubstationPadding(layoutParameters);
                /*graphY += layoutParameters.getInitialYBus()
                        + layoutParameters.getStackHeight()
                        + layoutParameters.getExternCellHeight()
                        + layoutParameters.getVerticalSpaceBus() * (g.getMaxV() + 2)
                        + getVerticalSubstationPadding(layoutParameters);*/
            }
            if (compactionType == ForceSubstationLayoutFactory.CompactionType.VERTICAL) {
                graphY /= 2;
            }
        }

        // Re-applying the substations layout, but with the voltage levels layout already created before
        final double oldHsp = layoutParameters.getHorizontalSubstationPadding();
        final double oldVsp = layoutParameters.getVerticalSubstationPadding();
        coordsSubstations
            .keySet()
            .stream()
            .forEach(s -> {
                s.getNodes().forEach(VoltageLevelGraph::resetCoords);
                LayoutParameters newLayoutParameters = new LayoutParameters(layoutParameters);
                double hsp = newCoords.get(s.getSubstationId()).get(X);
                double vsp = newCoords.get(s.getSubstationId()).get(Y);
                newLayoutParameters.setHorizontalSubstationPadding(hsp + oldHsp);
                newLayoutParameters.setVerticalSubstationPadding(vsp + oldVsp);
                // graphsLayouts.get(s).run(newLayoutParameters);
                runLayout(s, newLayoutParameters, layoutParameters);
            });

        // Calculate all the coordinates for the lines between the substation graphs
        manageSnakeLines(layoutParameters);
    }

    private void runLayout(SubstationGraph graph, LayoutParameters layoutParameters, LayoutParameters oldLayoutParameters) {
        // Calculate all the coordinates for the voltageLevel graphs in the substation graph
        double graphX = layoutParameters.getHorizontalSubstationPadding();
        double graphY = layoutParameters.getVerticalSubstationPadding();

        for (VoltageLevelGraph vlGraph : graph.getNodes()) {
            vlGraph.setX(graphX);
            vlGraph.setY(graphY);

            // Calculate the objects coordinates inside the voltageLevel graph
            VoltageLevelLayout vLayout = vLayoutFactory.create(vlGraph);
            vLayout.run(oldLayoutParameters);

            // Calculate the global coordinate of the voltageLevel graph
            Coord posVLGraph = calculateCoordVoltageLevel(oldLayoutParameters, vlGraph);

            graphX += posVLGraph.get(X) + getHorizontalSubstationPadding(oldLayoutParameters);
            // graphY += posVLGraph.get(Y) + getVerticalSubstationPadding(oldLayoutParameters);
            graphY += posVLGraph.get(Y);
        }
        // Calculate all the coordinates for the middle nodes and the snake lines between the voltageLevel graphs
        // manageSnakeLines(layoutParameters);
    }

    public void manageSnakeLines(LayoutParameters layoutParameters) {
        Map<String, Map<BusCell.Direction, Integer>> nbSnakeLinesTopBottom = new HashMap<>();
        zoneGraph
            .getNodes()
            .forEach(g ->
                nbSnakeLinesTopBottom.put(
                    g.getSubstationId(),
                    EnumSet.allOf(BusCell.Direction.class).stream().collect(Collectors.toMap(Function.identity(), v -> 0))
                )
            );
        Map<String, Integer> nbSnakeLinesBetween = zoneGraph
            .getNodes()
            .stream()
            .collect(Collectors.toMap(g -> g.getSubstationId(), v -> 0));

        zoneGraph.getNodes().forEach(g -> manageSnakeLines(g, layoutParameters, nbSnakeLinesTopBottom, nbSnakeLinesBetween));
        // manageSnakeLines(zoneGraph, layoutParameters, nbSnakeLinesTopBottom, nbSnakeLinesBetween);

        for (LineEdge lineEdge : zoneGraph.getLineEdges()) {
            LineEdge edgeSorted = new LineEdge(lineEdge.getLineId(), lineEdge.getNode1(), lineEdge.getNode2());
            edgeSorted.getNodes().sort(Comparator.comparingDouble(Node::getX));
            lineEdge.setSnakeLine(
                calculatePolylineSnakeLine(
                    layoutParameters,
                    edgeSorted.getNode1(),
                    edgeSorted.getNode2(),
                    nbSnakeLinesTopBottom,
                    nbSnakeLinesBetween
                )
            );
        }
    }

    private void manageSnakeLines(
        AbstractBaseGraph graph,
        LayoutParameters layoutParameters,
        Map<String, Map<BusCell.Direction, Integer>> nbSnakeLinesTopBottom,
        Map<String, Integer> nbSnakeLinesBetween
    ) {
        for (Node multiNode : graph.getMultiTermNodes()) {
            List<Edge> adjacentEdges = multiNode.getAdjacentEdges();
            List<Node> adjacentNodes = multiNode.getAdjacentNodes();
            if (adjacentNodes.size() == 2) {
                List<Double> pol = calculatePolylineSnakeLine(
                    layoutParameters,
                    adjacentNodes.get(0),
                    adjacentNodes.get(1),
                    nbSnakeLinesTopBottom,
                    nbSnakeLinesBetween
                );
                Coord coordNodeFict = new Coord(-1, -1);
                ((TwtEdge) adjacentEdges.get(0)).setSnakeLine(splitPolyline2(pol, 1, coordNodeFict));
                ((TwtEdge) adjacentEdges.get(1)).setSnakeLine(splitPolyline2(pol, 2, null));
                multiNode.setX(coordNodeFict.get(X), false);
                multiNode.setY(coordNodeFict.get(Y), false);
            } else if (adjacentNodes.size() == 3) {
                List<Double> pol1 = calculatePolylineSnakeLine(
                    layoutParameters,
                    adjacentNodes.get(0),
                    adjacentNodes.get(1),
                    nbSnakeLinesTopBottom,
                    nbSnakeLinesBetween
                );
                List<Double> pol2 = calculatePolylineSnakeLine(
                    layoutParameters,
                    adjacentNodes.get(1),
                    adjacentNodes.get(2),
                    nbSnakeLinesTopBottom,
                    nbSnakeLinesBetween
                );
                Coord coordNodeFict = new Coord(-1, -1);
                ((TwtEdge) adjacentEdges.get(0)).setSnakeLine(splitPolyline3(pol1, pol2, 1, coordNodeFict));
                ((TwtEdge) adjacentEdges.get(1)).setSnakeLine(splitPolyline3(pol1, pol2, 2, null));
                ((TwtEdge) adjacentEdges.get(2)).setSnakeLine(splitPolyline3(pol1, pol2, 3, null));
                multiNode.setX(coordNodeFict.get(X), false);
                multiNode.setY(coordNodeFict.get(Y), false);
            }
        }

        for (LineEdge lineEdge : graph.getLineEdges()) {
            List<Node> adjacentNodes = lineEdge.getNodes();
            lineEdge.setSnakeLine(
                calculatePolylineSnakeLine(
                    layoutParameters,
                    adjacentNodes.get(0),
                    adjacentNodes.get(1),
                    nbSnakeLinesTopBottom,
                    nbSnakeLinesBetween
                )
            );
        }
    }

    private List<Double> calculatePolylineSnakeLine(
        LayoutParameters layoutParameters,
        Node node1,
        Node node2,
        Map<String, Map<BusCell.Direction, Integer>> nbSnakeLinesTopBottom,
        Map<String, Integer> nbSnakeLinesBetween
    ) {
        ForceInfoCalcPoints info = new ForceInfoCalcPoints();
        info.setLayoutParam(layoutParameters);
        Optional<SubstationGraph> sub1 = zoneGraph
            .getNodes()
            .stream()
            .filter(x -> x.getVLGraph(node1.getGraph().getId()) == node1.getGraph())
            .findFirst();
        Optional<SubstationGraph> sub2 = zoneGraph
            .getNodes()
            .stream()
            .filter(x -> x.getVLGraph(node2.getGraph().getId()) == node2.getGraph())
            .findFirst();
        info.setsId1(sub1.get().getSubstationId()); // in this case i should use substation Id
        info.setsId2(sub2.get().getSubstationId()); // in this case i should use substation Id
        info.setdNode1(getNodeDirection(node1, 1));
        info.setdNode2(getNodeDirection(node2, 2));
        info.setNbSnakeLinesTopBottom(nbSnakeLinesTopBottom);
        info.setNbSnakeLinesBetween(nbSnakeLinesBetween);
        info.setX1(node1.getX());
        info.setX2(node2.getX());
        info.setY1(node1.getY());
        info.setInitY1(node1.getInitY() != -1 ? node1.getInitY() : node1.getY());
        info.setY2(node2.getY());
        info.setInitY2(node2.getInitY() != -1 ? node2.getInitY() : node2.getY());
        info.setxMaxGraph(Math.max(node1.getGraph().getX(), node2.getGraph().getX()));
        info.setIdMaxGraph(node1.getGraph().getX() > node2.getGraph().getX() ? sub1.get().getSubstationId() : sub2.get().getSubstationId());

        return calculatePolylinePoints(info);
    }

    public static List<Double> calculatePolylinePoints(ForceInfoCalcPoints info) {
        List<Double> pol = new ArrayList<>();

        LayoutParameters layoutParam = info.getLayoutParam();
        BusCell.Direction dNode1 = info.getdNode1();
        BusCell.Direction dNode2 = info.getdNode2();
        String sId1 = info.getsId1();
        String sId2 = info.getsId2();
        Map<String, Map<BusCell.Direction, Integer>> nbSnakeLinesTopBottom = info.getNbSnakeLinesTopBottom();
        Map<String, Integer> nbSnakeLinesBetween = info.getNbSnakeLinesBetween();
        double x1 = info.getX1();
        double x2 = info.getX2();
        double y1 = info.getY1();
        double initY1 = info.getInitY1();
        double y2 = info.getY2();
        double initY2 = info.getInitY2();
        double xMaxGraph = info.getxMaxGraph();
        String idMaxGraph = info.getIdMaxGraph();

        switch (dNode1) {
            case BOTTOM:
                if (dNode2 == BusCell.Direction.BOTTOM) { // BOTTOM to BOTTOM
                    nbSnakeLinesTopBottom.get(sId1).compute(dNode1, (k, v) -> v + 1);
                    nbSnakeLinesTopBottom.get(sId2).compute(dNode2, (k, v) -> v + 1);
                    double decalV1 = nbSnakeLinesTopBottom.get(sId1).get(dNode1) * layoutParam.getVerticalSnakeLinePadding();
                    double decalV2 = nbSnakeLinesTopBottom.get(sId2).get(dNode2) * layoutParam.getVerticalSnakeLinePadding();

                    double yDecal = Math.max(initY1 + decalV1, initY2 + decalV2);

                    pol.addAll(Arrays.asList(x1, y1, x1, yDecal, x2, yDecal, x2, y2));
                } else { // BOTTOM to TOP
                    if (y1 < y2) {
                        nbSnakeLinesTopBottom.get(sId1).compute(dNode1, (k, v) -> v + 1);
                        nbSnakeLinesTopBottom.get(sId2).compute(dNode2, (k, v) -> v + 1);
                        double decalV1 = nbSnakeLinesTopBottom.get(sId1).get(dNode1) * layoutParam.getVerticalSnakeLinePadding();
                        double decalV2 = nbSnakeLinesTopBottom.get(sId2).get(dNode2) * layoutParam.getVerticalSnakeLinePadding();

                        double yDecal = Math.max(initY1 + decalV1, initY2 - decalV2);

                        pol.addAll(Arrays.asList(x1, y1, x1, yDecal, x2, yDecal, x2, y2));
                    } else {
                        nbSnakeLinesTopBottom.get(sId1).compute(dNode1, (k, v) -> v + 1);
                        nbSnakeLinesTopBottom.get(sId2).compute(dNode2, (k, v) -> v + 1);
                        nbSnakeLinesBetween.compute(idMaxGraph, (k, v) -> v + 1);

                        double decalV1 = nbSnakeLinesTopBottom.get(sId1).get(dNode1) * layoutParam.getVerticalSnakeLinePadding();
                        double decalV2 = nbSnakeLinesTopBottom.get(sId2).get(dNode2) * layoutParam.getVerticalSnakeLinePadding();
                        double xBetweenGraph =
                            xMaxGraph - (nbSnakeLinesBetween.get(idMaxGraph) * layoutParam.getHorizontalSnakeLinePadding());

                        pol.addAll(
                            Arrays.asList(
                                x1,
                                y1,
                                x1,
                                initY1 + decalV1,
                                xBetweenGraph,
                                initY1 + decalV1,
                                xBetweenGraph,
                                initY2 - decalV2,
                                x2,
                                initY2 - decalV2,
                                x2,
                                y2
                            )
                        );
                    }
                }
                break;
            case TOP:
                if (dNode2 == BusCell.Direction.TOP) { // TOP to TOP
                    nbSnakeLinesTopBottom.get(sId1).compute(dNode1, (k, v) -> v + 1);
                    nbSnakeLinesTopBottom.get(sId2).compute(dNode2, (k, v) -> v + 1);
                    double decalV1 = nbSnakeLinesTopBottom.get(sId1).get(dNode1) * layoutParam.getVerticalSnakeLinePadding();
                    double decalV2 = nbSnakeLinesTopBottom.get(sId2).get(dNode2) * layoutParam.getVerticalSnakeLinePadding();

                    double yDecal = Math.min(initY1 - decalV1, initY2 - decalV2);

                    pol.addAll(Arrays.asList(x1, y1, x1, yDecal, x2, yDecal, x2, y2));
                } else { // TOP to BOTTOM
                    if (y1 > y2) {
                        nbSnakeLinesTopBottom.get(sId1).compute(dNode1, (k, v) -> v + 1);
                        nbSnakeLinesTopBottom.get(sId2).compute(dNode2, (k, v) -> v + 1);
                        double decalV1 = nbSnakeLinesTopBottom.get(sId1).get(dNode1) * layoutParam.getVerticalSnakeLinePadding();
                        double decalV2 = nbSnakeLinesTopBottom.get(sId2).get(dNode2) * layoutParam.getVerticalSnakeLinePadding();

                        double yDecal = Math.min(initY1 - decalV1, initY2 + decalV2);

                        pol.addAll(Arrays.asList(x1, y1, x1, yDecal, x2, yDecal, x2, y2));
                    } else {
                        nbSnakeLinesTopBottom.get(sId1).compute(dNode1, (k, v) -> v + 1);
                        nbSnakeLinesTopBottom.get(sId2).compute(dNode2, (k, v) -> v + 1);
                        nbSnakeLinesBetween.compute(idMaxGraph, (k, v) -> v + 1);
                        double decalV1 = nbSnakeLinesTopBottom.get(sId1).get(dNode1) * layoutParam.getVerticalSnakeLinePadding();
                        double decalV2 = nbSnakeLinesTopBottom.get(sId2).get(dNode2) * layoutParam.getVerticalSnakeLinePadding();

                        double xBetweenGraph =
                            xMaxGraph - (nbSnakeLinesBetween.get(idMaxGraph) * layoutParam.getHorizontalSnakeLinePadding());

                        pol.addAll(
                            Arrays.asList(
                                x1,
                                y1,
                                x1,
                                initY1 - decalV1,
                                xBetweenGraph,
                                initY1 - decalV1,
                                xBetweenGraph,
                                initY2 + decalV2,
                                x2,
                                initY2 + decalV2,
                                x2,
                                y2
                            )
                        );
                    }
                }
                break;
            default:
        }
        return pol;
    }

    @Override
    protected List<Double> splitPolyline3(List<Double> pol1, List<Double> pol2, int numPart, Coord coord) {
        List<Double> res = new ArrayList<>();

        if (numPart == 1 || numPart == 2) {
            res = super.splitPolyline3(pol1, pol2, numPart, coord);
        } else {
            // the third new edge now begins with the fictitious node point
            res.add(pol1.get(pol1.size() - 4));
            res.add(pol1.get(pol1.size() - 3));
            // then we add an intermediate point with the absciss of the third point in the original second polyline
            // and the ordinate of the fictitious node
            res.add(pol2.get(4));
            res.add(pol1.get(pol1.size() - 3));
            // then we had the last three or two points of the original second polyline
            if (pol2.size() > 8) {
                res.addAll(pol2.subList(pol2.size() - 6, pol2.size()));
            } else {
                res.addAll(pol2.subList(pol2.size() - 2, pol2.size()));
            }
        }

        return res;
    }

    @Override
    protected Coord calculateCoordVoltageLevel(LayoutParameters layoutParameters, VoltageLevelGraph voltageLevelGraph) {
        int maxH = voltageLevelGraph.getMaxH() / 3;
        return new Coord(layoutParameters.getInitialXBus() + (maxH + 2) * layoutParameters.getCellWidth(), 0);
    }

    @Override
    protected double getHorizontalSubstationPadding(LayoutParameters layoutParameters) {
        return layoutParameters.getHorizontalSubstationPadding();
    }

    @Override
    protected double getVerticalSubstationPadding(LayoutParameters layoutParameters) {
        return layoutParameters.getVerticalSubstationPadding();
    }

    @Override
    protected List<Double> calculatePolylineSnakeLine(
        LayoutParameters layoutParameters,
        Node node,
        Node node1,
        InfosNbSnakeLines infosNbSnakeLines,
        boolean b
    ) {
        return Collections.emptyList();
    }
}
