function rwd_fig(rwd)
%% display orignal value
figure;
hold on;
plot(rwd,'r.','MarkerSize',0.5);
xlabel('Episodes'); % x-axis label
ylabel('Sum of rewards during episode'); % y-axis label
hold off;

%% display statistical features
figure;
hold on;
xlabel('Episodes'); % x-axis label
ylabel('Sum of rewards during episode'); % y-axis label
step = 50;
value_len = length(rwd);
itr = ceil(value_len/step);
mean_value(itr) = 0;
std_d(itr) = 0;
for i = 1:itr-1
    y = hampel(rwd((i-1)*step+1:i*step));
    mean_value(i) = mean(y);
    std_d(i) = std(y);
end
y = hampel(rwd((itr-1)*step+1:value_len));
mean_value(itr) = mean(y);
std_d(itr) = std(y);
x = step/2:step:value_len;
errorbar(x,mean_value,std_d,'-s','MarkerSize',4,...
    'MarkerEdgeColor','red','LineWidth', 1);
hold off;