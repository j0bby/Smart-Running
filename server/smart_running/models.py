import djchoices
from django.contrib.auth.models import User
from django.core.validators import MaxValueValidator, MinValueValidator
from django.db import models
from django.db.models.signals import post_save
from django.dispatch import receiver


class ModeType(djchoices.DjangoChoices):
    TOURISTIC = djchoices.ChoiceItem()
    SPORTY = djchoices.ChoiceItem()


class ProfileType(djchoices.DjangoChoices):
    TOURISTIC = djchoices.ChoiceItem()
    SPORTY = djchoices.ChoiceItem()
    BOTH = djchoices.ChoiceItem()


class Route(models.Model):
    publisher = models.ForeignKey(User)

    date_published = models.DateTimeField(auto_now_add=True)
    last_updated = models.DateTimeField(auto_now=True)

    title = models.CharField(max_length=64)
    description = models.CharField(max_length=200)

    mode = models.CharField(max_length=16, choices=ModeType.choices)

    difficulty = models.IntegerField(validators=[
        MinValueValidator(1), MaxValueValidator(5)
    ])

    # markers list found in Marker model

    def __str__(self):
        return "%s: %s (%d markers)" % (self.title, self.description, len(self.markers))


class Marker(models.Model):
    title = models.CharField(max_length=64)
    description = models.CharField(max_length=200)
    full_description = models.TextField()

    latitude = models.DecimalField(max_digits=9, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)

    zone_radius = models.FloatField()

    routes = models.ManyToManyField(Route, related_name='markers')

    def __str__(self):
        return "%s: %s" % (self.title, self.description)


class UserProfile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)

    profile_type = models.CharField(max_length=16, choices=ProfileType.choices)
    birth_date = models.DateField(null=True)
    email_verified = models.BooleanField(default=False)


@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        UserProfile.objects.create(user=instance)


@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    instance.userprofile.save()
