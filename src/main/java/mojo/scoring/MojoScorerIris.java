package mojo.scoring;

import ai.h2o.mojos.runtime.MojoPipeline;
import ai.h2o.mojos.runtime.frame.MojoFrame;
import ai.h2o.mojos.runtime.frame.MojoFrameBuilder;
import ai.h2o.mojos.runtime.frame.MojoRowBuilder;


public class MojoScorerIris {

    public static final String MOJO_PATH = "src/main/resources/h2o/pipeline.mojo";

    public static void main(String[] args) throws Exception {
        // Load model and csv
        MojoPipeline model = MojoPipeline.loadFrom(MOJO_PATH);

        // Get and fill the input columns
        //5.5,2.4,3.7,1.0,versicolor
        MojoFrameBuilder frameBuilder = model.getInputFrameBuilder();
        MojoRowBuilder   rowBuilder   = frameBuilder.getMojoRowBuilder();
        rowBuilder.setValue("sepal_length", "5.5");
        rowBuilder.setValue("sepal_width", "2.4");
        rowBuilder.setValue("petal_length", "3.7");
        rowBuilder.setValue("petal_width", "1.0");

        frameBuilder.addRow(rowBuilder);

        // Create a frame which can be transformed by MOJO pipeline
        MojoFrame iframe = frameBuilder.toMojoFrame();

        // Transform input frame by MOJO pipeline
        MojoFrame oframe = model.transform(iframe);
        // `MojoFrame.debug()` can be used to view the contents of a Frame
        // oframe.debug();

        int numColumns = oframe.getNcols();
        for (int i = 0; i < numColumns; i++) {
            System.out.println(oframe.getColumnName(i) + ":");
            for (String s : oframe.getColumn(i).getDataAsStrings()) {
                System.out.println(s);
            }
        }
    }
}
