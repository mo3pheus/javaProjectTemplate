
package mojo.scoring;

import ai.h2o.mojos.runtime.MojoPipeline;
import ai.h2o.mojos.runtime.frame.MojoFrame;
import ai.h2o.mojos.runtime.frame.MojoFrameBuilder;
import ai.h2o.mojos.runtime.frame.MojoRowBuilder;


public class MojoScorerIris {

    public static final String MOJO_PATH = "/home/sanket/Documents/workspace/javaProjectTemplate/src/main/resources/h2o/pipeline.mojo";

    public static void main(String[] args) throws Exception {
        // Load model and csv
        MojoPipeline model = MojoPipeline.loadFrom(MOJO_PATH);

        // Get and fill the input columns
        //4.9,3,1.4,0.2,setosa
        MojoFrameBuilder frameBuilder = model.getInputFrameBuilder();
        MojoRowBuilder   rowBuilder   = frameBuilder.getMojoRowBuilder();
        rowBuilder.setValue("sepal_length", "4.9");
        rowBuilder.setValue("sepal_width", "3.0");
        rowBuilder.setValue("petal_length", "1.4");
        rowBuilder.setValue("petal_width", "0.2");

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
