# RSABoy
This is a simple application for asymmetric encryption.

## How to use by a case
### Case
Bolang wants to send a message to Batman. He wants to say "Criminals are in action on Rabbit Road"

### Step 1
- They must have private key and public key
#### Bolang 
```
$ rsaboy.jar '{"command": "generate_key", "filename": "Bolang"}'
```
Above command will generate `Bolang.priv.pem` and `Bolang.pub.pem` in Bolang's device.

#### Batman
```
$ rsaboy.jar '{"command": "generate_key", "filename": "Batman"}'
```
Above command will generate `Batman.priv.pem` and `Batman.pub.pem` in Batman's device.

### Step 2
In this step they have to share their public key
#### Bolang
Bolang send his public key `Bolang.pub.pem` to Batman

#### Batman
Batman send his public key `Batman.pub.pem` to Bolang

### Step 3
#### Bolang
Encrypt the message using Batman's public key
```
$ rsaboy.jar '{"command": "encrypt", "message": "Criminals are in action on Rabbit Road", "pubkey_file": "Batman.pub.pem"}'
SKl21Quj/bGR1HMRNrc/qiNAFXIU8RAhBfJdQNmSwgHLKw1KFVfu78+jcQ8UAsXtG6hd8ih9y+Kpdu8aDSxGIh4MNaTBlibfuODdvjhRvBJEr+iX6rzN+2q3+2bwD/gyckePhdGyXNGEmQXTUCf9RWrsVur4gRNdzl5xiE/jQxDhrBo5IN8okTLYJ/5AQNQh2Ic7Rm91M4N70wOM/M0FWzrwFmBTFuQ3pC2+ZeMJfHR5KQ3EP1dNo4xA8GDjgSkE6fmkyWhbQspT3cLUKCHFfna9N+m8x0icbZ7MYFnzTREWrgewpNjuTFH9hmxnLQ7DWulyOlgR48V0RPQBCVr8Dg==
```
Sign the message
```
$ rsaboy.jar '{"command": "sign", "message": "Criminals are in action on Rabbit Road", "privkey_file": "Bolang.priv.pem"}'
btT3N2EFuokJUqXfW0IN9MrGe0pi/FuvQ3sFE987/B2OKHuzZnrLl0YVfRp1i3O4Nnrhf9AfCI9ELxQQ2OENDIPQa2iA8/hKEh23IwzZyYBCQGRHc6Gzreoo5OA3IvsXABtJLXJKaNJuKvwpIl0/lTMyqbxZt7Etbfh+LsOovXzSRlDwwvOWLPk5XF8b9UFJm9IWar/EsXhO4Ee6Me49nGc7CDApwzOvv1mufn36MyrdrmQGW6W9YYme58ZBWzYu9/UmH97JLcyXS/X6uYk/iXqWUC81uBO/QA0VpaaUkVXmwvKYLSHhzrEF5ubUrYsxGT/QSkbLK2IwPBD4kDYJUg==
```
Than he send the encrypted message and the digatal signature to Batman


### Step 4
#### Batman
Decrypt the message using His private key.
```
$ rsaboy.jar '{"command": "decrypt", "message": "SKl21Quj/bGR1HMRNrc/qiNAFXIU8RAhBfJdQNmSwgHLKw1KFVfu78+jcQ8UAsXtG6hd8ih9y+Kpdu8aDSxGIh4MNaTBlibfuODdvjhRvBJEr+iX6rzN+2q3+2bwD/gyckePhdGyXNGEmQXTUCf9RWrsVur4gRNdzl5xiE/jQxDhrBo5IN8okTLYJ/5AQNQh2Ic7Rm91M4N70wOM/M0FWzrwFmBTFuQ3pC2+ZeMJfHR5KQ3EP1dNo4xA8GDjgSkE6fmkyWhbQspT3cLUKCHFfna9N+m8x0icbZ7MYFnzTREWrgewpNjuTFH9hmxnLQ7DWulyOlgR48V0RPQBCVr8Dg==", "privkey_file": "Batman.priv.pem"}'
Criminals are in action on Rabbit Road 
```
Verifying the message is really came from Bolang. It uses Bolang's public key
```
$ rsaboy.jar '{"command": "verify", "message": "Criminals are in action on Rabbit Road", "signature":"btT3N2EFuokJUqXfW0IN9MrGe0pi/FuvQ3sFE987/B2OKHuzZnrLl0YVfRp1i3O4Nnrhf9AfCI9ELxQQ2OENDIPQa2iA8/hKEh23IwzZyYBCQGRHc6Gzreoo5OA3IvsXABtJLXJKaNJuKvwpIl0/lTMyqbxZt7Etbfh+LsOovXzSRlDwwvOWLPk5XF8b9UFJm9IWar/EsXhO4Ee6Me49nGc7CDApwzOvv1mufn36MyrdrmQGW6W9YYme58ZBWzYu9/UmH97JLcyXS/X6uYk/iXqWUC81uBO/QA0VpaaUkVXmwvKYLSHhzrEF5ubUrYsxGT/QSkbLK2IwPBD4kDYJUg==", "pubkey_file": "Bolang.pub.pem"}'
true
```
### Step n
#### Batman
He can reply to Bolang's message. All He has to do is encrypt the message and give a digital sign like step number 3.

Steps 3 and 4 will be repeated continuously if Bolang and Batman continue the conversation. 