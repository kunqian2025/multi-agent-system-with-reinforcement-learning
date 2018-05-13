%%
clear clc
closepreview

% Setup parameters
cameraWidth = 1600;  %fixed camera resolution
cameraHeight = 1200; %fixed camera resolution
ROIWidth = 1400;      %Region of interest (resolution for image processing
ROIHeight = 800;
XOffset = cameraWidth/2 - ROIWidth/2;   %centering ROI
YOffset = (cameraHeight/2 - ROIHeight/2) - 90;


%Start gigE video object
vid = videoinput('gige', 1, 'BayerBG8');

src = getselectedsource(vid);
vid.FramesPerTrigger = 1;
%vid.FramesPerTrigger = Inf;

%ROI size
vid.ROIPosition = [XOffset YOffset ROIWidth ROIHeight];
src.DSPSubregionRight = ROIWidth;
src.DSPSubregionBottom = ROIHeight;

%Exposure and brightness gain
src.Gain = 0;
src.ExposureTimeAbs = 10000;

%fps (sets to fps limit)
FPSLimit = src.AcquisitionFrameRateLimit;
src.AcquisitionFrameRateAbs = FPSLimit;

start(vid)
preview(vid);


%% Image processing
closepreview

Player_Original = vision.VideoPlayer();
Player_Processed = vision.VideoPlayer();
runLoop = true;

%Color Threshold
hueTreshMax = 1;
hueTreshMin = 0;
satTreshMax = 1;
satTreshMin = 0;
valTreshMax = 1;
valTreshMin = 0;

h = SetupSlider();


%
hblob = vision.BlobAnalysis('AreaOutputPort',false, ...
'CentroidOutputPort', true, ...
'BoundingBoxOutputPort', false, ...
'MinimumBlobArea', 100, ...
'MaximumBlobArea', ROIWidth*ROIHeight, ...
'MaximumCount', 1);

while(runLoop)

    orgImage = getsnapshot(vid);
    % Convert RGB image to HSV and extract each color
	hsvImage = rgb2hsv(orgImage);
    hImage = hsvImage(:,:,1);
	sImage = hsvImage(:,:,2);
	vImage = hsvImage(:,:,3);
    
    % Apply selected Threshhold values to create binary images (MASK)
    hMask = (hImage >= hueTreshMin) & (hImage <= hueTreshMax);
	sMask = (sImage >= satTreshMin) & (sImage <= satTreshMax);
	vMask = (vImage >= valTreshMin) & (vImage <= valTreshMax);
    FullMask = uint8(hMask & sMask & vMask);
    
    % Filter image
    structuringElement = strel('disk', 4);
	FullMask_Filter1 = imclose(FullMask, structuringElement);    
    FullMask_Filter2 = uint8(imfill(logical(FullMask_Filter1), 'holes'));    
    
    %Create final image to track object on
    Bw = imbinarize(FullMask_Filter2);
    
    %Find center of object 
    centroid = step(hblob,Bw)
    
    % Draw marker
    orgImage = insertMarker(orgImage, centroid, 'plus','size',20);
    % Display video frame.
    step(Player_Original, orgImage);
    step(Player_Processed, Bw);
    
    % Check whether the video player window has been closed.
    runLoop = isOpen(Player_Original);
    %disp([hueTreshMax,hueTreshMin,satTreshMax,satTreshMin,valTreshMax,valTreshMin]);
    
    
end

flushdata(vid);
clear all
