# multi-agent-system-with-reinforcement-learning
The project files here are all used in my thesis, "Methods and implementations for coordinated multi-agent learning". In another repository "reinforcement-learning", the implementations for popular single agent and multi-agents environment are shown. Here, multi-agent actor-critic method is used in an agent-based system. For each folder inside this repository:

## CameraVision
It is Simulink block implemented in MATLAB for track the locations of rolling robots--Spheros.

## Parameters Learning
It is MATLAB simulation for getting the parameter vectors for controlling the Spheros.

## MAS_OneContainer
It is an agent-based system simulates the communication among agents in the model that I am dealing with. In this project, the agents are situated in one container--the main conrainer, in my PC.

## MAS_Basic
It is also an agent-based system the same as above, but, the agents are situated in different containers: the broker agent is in the main container in my PC, while the two robot agents are in two Raspberry Pi. 

## MAS_RL
It is the final implementation for an embedded design of an agent based system. And the moving of the Sphero is controlled by reinforcement learning method--MAAC.

The video is also uploaded in this repository, for viewing it online, click the link: https://www.youtube.com/watch?v=7po-JgCF4vE
