import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.IJ;
import ij.process.ImageProcessor;

public class JoinChannelsWithLUT_ implements PlugIn{
	private ImagePlus redChannel, greenChannel, blueChannel, newOutput;
	private ImageProcessor redProcessor, greenProcessor, blueProcessor, outputProcessor;
	
	public void run(String arg) {	
		if (this.getImagesAndProcessors()) {
			this.joinChannels();	
		}				
	}
	
	private void joinChannels(){		
		int x, y, pixelValue[] = {0,0,0};		
		this.createOutput();				
		
		for (x = 0; x < this.outputProcessor.getWidth(); x++) {
			for (y = 0; y < this.outputProcessor.getHeight(); y++) {
				pixelValue[0] = this.redProcessor.getPixel(x, y);
				pixelValue[1] = this.greenProcessor.getPixel(x, y);
				pixelValue[2] = this.blueProcessor.getPixel(x, y);
				this.outputProcessor.putPixel(x, y, pixelValue);
			}
		}		
		newOutput.show();		
	}
	
	private void createOutput() {		
		this.newOutput = IJ.createImage("RGB joined image", "RGB", redChannel.getWidth(), redChannel.getHeight(), 1);
		this.outputProcessor = this.newOutput.getProcessor();
	}
	
	private boolean getImagesAndProcessors() {
		String title;		
		for (int i=0; i<3; i++) {
			if (IJ.getImage().getType() == ImagePlus.GRAY8) {
				title = IJ.getImage().getTitle();			
				switch (title) {
				case "RED":
					this.redChannel = IJ.getImage();				
					this.redProcessor = this.redChannel.getProcessor();
					this.redChannel.close();				
					break;
				case "GREEN":
					this.greenChannel = IJ.getImage();				
					this.greenProcessor = this.greenChannel.getProcessor();
					this.greenChannel.close();				
					break;				
				case "BLUE":
					this.blueChannel = IJ.getImage();
					this.blueProcessor = this.blueChannel.getProcessor();
					this.blueChannel.close();				
					break;				
				default:
					IJ.error("In order to run this plugin. There should be three images of each RGB channel named as 'RED','GREEN' and 'BLUE' respectively");
					return false;
				}
			}
			else {
				IJ.error("The image must be Type GRAY8 in order to run this plugin");
				return false;
			}
		}
		if (this.redChannel == null || this.greenChannel == null || this.blueChannel == null) {
			IJ.error("Error while getting RGB channels. Cannot proceed!");
			return false;
		}
		return true;
	}
}