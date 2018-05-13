function sliderupdate(varargin)
%ht = findobj(gcbf,'style','text');
ht = findobj(gcbf,'tag',['text_' get(varargin{1},'tag')]);
v = get(varargin{1},'value');
set(ht, 'string', [get(varargin{1},'tag') ' :' num2str(v)])
assignin('base', get(varargin{1},'tag'), v)
disp(varargin{1})
end