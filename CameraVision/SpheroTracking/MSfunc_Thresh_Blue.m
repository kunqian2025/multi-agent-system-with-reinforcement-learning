function MSfunc_Tresh_Blue(block)
% Level-2 M-File S-Functions, Copyright 2015, The MathWorks, Inc.
setup(block);

%% ---------------------------------------------------------

    function setup(block)
        % Register the number of ports.
        block.NumInputPorts  = 3;
        block.NumOutputPorts = 1;
        block.NumDialogPrms  = 0;
        
        % Setup port properties to be inherited or dynamic
        block.SetPreCompInpPortInfoToDynamic;
        block.SetPreCompOutPortInfoToDynamic;      
                
        block.OutputPort(1).DatatypeID  = 8;  % Binary
                  
              
        % Register sample times
        %  [0 offset]            : Continuous sample time
        %  [positive_num offset] : Discrete sample time
        %
        %  [-1, 0]               : Inherited sample time
        %  [-2, 0]               : Variable sample time
        block.SampleTimes = [-1 0];                 
        
        block.RegBlockMethod('Outputs', @Output); % called first in sim loop
     
    end


%%
    function Output(block)
        

        % Blue 0 0 255
        hueTreshMin = 0.55;
        hueTreshMax = 0.70;
        satTreshMin = 0.15;
        satTreshMax = 1;
        valTreshMin = 0.15;
        valTreshMax = 1; 

        
        hImage = block.InputPort(1).Data;
        sImage = block.InputPort(2).Data;
        vImage = block.InputPort(3).Data;
        %orgImage = block.InputPort(4).Data;
        
        % Apply selected Threshhold values to create binary images (MASK)
        hMask = (hImage >= hueTreshMin) & (hImage <= hueTreshMax);
        sMask = (sImage >= satTreshMin) & (sImage <= satTreshMax);
        vMask = (vImage >= valTreshMin) & (vImage <= valTreshMax);
        FullMask = uint8(hMask & sMask & vMask);
    
        % Filter image
        %structuringElement = strel('disk', 4);
        %FullMask_Filter1 = imclose(FullMask, structuringElement);    
        %FullMask_Filter2 = uint8(imfill(logical(FullMask_Filter1), 'holes'));    
    
        %Create final image to track object on
        Bw = imbinarize(FullMask);
      
        block.OutputPort(1).Data = Bw;
        
    end

end