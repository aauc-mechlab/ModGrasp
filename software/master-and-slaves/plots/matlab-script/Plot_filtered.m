%Copyright Filippo Sanfilippo

clc, clear all

Cparameter = 0.594/160;
Kt = 0.5;

data = load('balloon.txt');
[r,c] = size(data);

C = data * Cparameter;
T = C * Kt;

iterations = 0:(r-1);
time = linspace(1,6,length(iterations));    %change the parameter according to time the experiment has last

cc=hsv(12);

figure(1);


for i=1:1:c
    plot(time, C(:,i),'color',cc(i,:));
    hold on;
end


Legend=cell(c,1);
for j=1:1:c
    Legend{j}=strcat('J', num2str(j));
end
legend(Legend);

%title('Current');
xlabel('Time[s]')
ylabel('Current[A]')
hold off;

figure(2);
for i=1:1:c
    plot(time, T(:,i),'color',cc(i,:));
    hold on;
end

Legend=cell(c,1);
for j=1:1:c
    Legend{j}=strcat('J', num2str(j));
end
legend(Legend);

%title('Torque');
xlabel('Time[s]')
ylabel('Torque[Nm]')
hold off;

% Design the filter and view the filter's magnitude response
fc = 2; %change this parameter to increase or decrease effectiveness of the filter
Wn = (2/r)*fc;
b = fir1(20,Wn,'low',kaiser(21,3));
fvtool(b,1,'Fs',r);
%------
% Apply the filter to the signal and plot the result for the first ten periods of the 100-Hz sinusoid.
Tf = filter(b,1,T);
%-----

figure(3);

for i=1:1:c
    plot(time, Tf(:,i), '--mo');
    hold on;
end

Legend=cell(c,1);
for j=1:1:c
    Legend{j}=strcat('J', num2str(j));
end
legend(Legend);

%title('Torque');
xlabel('Time[s]')
ylabel('Torque[Nm]')
hold off;